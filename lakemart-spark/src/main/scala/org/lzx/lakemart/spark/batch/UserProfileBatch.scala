package org.lzx.lakemart.spark.batch

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import java.util.Properties

object UserProfileBatch {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("UserProfileBatch")
      .master("local[*]")
      .config("spark.ui.enabled", "false")
      .config("spark.sql.extensions", "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions")
      .config("spark.sql.catalog.lake", "org.apache.iceberg.spark.SparkCatalog")
      .config("spark.sql.catalog.lake.type", "hadoop")
      .config("spark.sql.catalog.lake.warehouse", "s3a://lakemart-warehouse/iceberg")
      .config("spark.hadoop.fs.s3a.endpoint", "http://localhost:9000")
      .config("spark.hadoop.fs.s3a.access.key", "minioadmin")
      .config("spark.hadoop.fs.s3a.secret.key", "minioadmin")
      .config("spark.hadoop.fs.s3a.path.style.access", "true")
      .config("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
      .getOrCreate()

    import spark.implicits._

    // 1. 读取用户订单数据（从 MySQL 读取 tb_order）
    val jdbcUrl = "jdbc:mysql://localhost:3307/lakemart?useSSL=false&serverTimezone=Asia/Shanghai"
    val props = new Properties()
    props.setProperty("user", "root")
    props.setProperty("password", "root")
    props.setProperty("driver", "com.mysql.cj.jdbc.Driver")

    // 读取所有用户
    val usersDF = spark.read.jdbc(jdbcUrl, "tb_user", props)
      .select($"id".as("user_id"), $"create_time".as("reg_time"))

    // 读取订单数据（已支付/已完成状态）
    val ordersDF = spark.read.jdbc(jdbcUrl, "tb_order", props)
      .where($"status".isin(1, 2, 3))
      .select($"id", $"user_id", $"total_amount", $"create_time".as("order_time"))   // ✅ 加入 id
    // 读取订单项（获取商品品类）
    val orderItemsDF = spark.read.jdbc(jdbcUrl, "tb_order_item", props)
      .select($"order_id", $"product_id", $"price", $"quantity")

    // 读取商品分类关联
    val productsDF = spark.read.jdbc(jdbcUrl, "tb_product", props)
      .select($"id".as("product_id"), $"category_id")

    // 2. 计算订单统计指标（所有用户）
    val orderStatsDF = ordersDF.groupBy($"user_id")
      .agg(
        count("*").as("total_order_count"),
        sum($"total_amount").as("total_amount"),
        avg($"total_amount").as("avg_order_amount"),
        max($"total_amount").as("max_order_amount"),
        max($"order_time").as("last_order_time")
      )
      .withColumn("last_order_days", datediff(current_date(), $"last_order_time"))

    // 3. 计算偏好品类（按品类购买次数排名 Top3）
    val categoryOrdersDF = orderItemsDF
      .join(ordersDF.select($"id".as("order_id"), $"user_id"), Seq("order_id"))
      .join(productsDF, Seq("product_id"))
      .groupBy($"user_id", $"category_id")
      .agg(count("*").as("cat_count"))
      .withColumn("rank", row_number().over(org.apache.spark.sql.expressions.Window.partitionBy($"user_id").orderBy($"cat_count".desc)))
      .filter($"rank" <= 3)

    // 4. 读取行为日志（近30天）
    val behaviorDF = spark.read.jdbc(jdbcUrl, "user_behavior_log", props)
      .where($"create_time" >= date_sub(current_date(), 30))
      .select($"user_id", $"create_time".as("behavior_time"), $"action")
      .withColumn("behavior_date", to_date($"behavior_time"))

    // 计算近7天行为次数
    val behavior7dDF = behaviorDF
      .where($"behavior_time" >= date_sub(current_date(), 7))
      .groupBy($"user_id")
      .agg(count("*").as("action_count_7d"))

    // 计算近30天行为次数和活跃天数
    val behavior30dDF = behaviorDF
      .groupBy($"user_id")
      .agg(
        count("*").as("action_count_30d"),
        countDistinct($"behavior_date").as("active_days_7d")  // 近30天活跃天数（字段名复用）
      )

    // 5. 关联所有数据，生成画像
    val profileDF = usersDF
      .join(orderStatsDF, Seq("user_id"), "left")
      .join(behavior7dDF, Seq("user_id"), "left")
      .join(behavior30dDF, Seq("user_id"), "left")
      .na.fill(0)

    // 6. 计算生命周期阶段
    val profileWithLifecycleDF = profileDF
      .withColumn("lifecycle_stage",
        when($"total_order_count" === 0 && $"action_count_30d" > 0 && datediff(current_date(), $"reg_time") <= 30, "新用户")
          .when($"total_order_count" > 0 && $"total_order_count" <= 3 && $"action_count_30d" > 0, "成长用户")
          .when($"total_order_count" > 3 && $"action_count_30d" > 0, "成熟用户")
          .when($"action_count_30d" === 0 && $"last_order_days" <= 30, "沉默用户")
          .when($"action_count_30d" === 0 && $"last_order_days" > 30 && $"last_order_days" <= 90, "沉睡用户")
          .when($"action_count_30d" === 0 && $"last_order_days" > 90, "流失用户")
          .otherwise("未知")
      )

    // 7. 计算用户等级（根据总消费）
    val profileWithLevelDF = profileWithLifecycleDF
      .withColumn("user_level",
        when($"total_amount" >= 10000, "钻石")
          .when($"total_amount" >= 5000, "黄金")
          .when($"total_amount" >= 1000, "白银")
          .when($"total_amount" >= 100, "青铜")
          .otherwise("新用户")
      )

    // 8. 准备写入 MySQL（简化版，只保留核心字段）
    val resultDF = profileWithLevelDF.select(
      $"user_id",
      $"total_order_count",
      $"total_amount",
      $"avg_order_amount",
      $"max_order_amount",
      $"last_order_days",
      $"action_count_7d",
      $"action_count_30d",
      $"active_days_7d",
      $"lifecycle_stage",
      $"user_level"
    )

    // 9. 清空并写入 user_profile 表
    import java.sql.{DriverManager, Statement}
    var conn: java.sql.Connection = null
    var stmt: Statement = null
    try {
      conn = DriverManager.getConnection(jdbcUrl, props)
      stmt = conn.createStatement()
      stmt.executeUpdate("TRUNCATE TABLE user_profile")
    } finally {
      if (stmt != null) stmt.close()
      if (conn != null) conn.close()
    }

    resultDF.write.mode("append").jdbc(jdbcUrl, "user_profile", props)

    spark.stop()
  }
}