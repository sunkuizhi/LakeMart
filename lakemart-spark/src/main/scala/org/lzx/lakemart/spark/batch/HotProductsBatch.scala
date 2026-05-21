package org.lzx.lakemart.spark.batch

import org.apache.spark.sql.SparkSession
import java.util.Properties

object HotProductsBatch {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("HotProductsBatch")
      .master("local[*]")
      .config("spark.ui.enabled", "false")   // 添加这一行
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

    // 查询最近1小时热销商品 TOP 10
    val hotProducts = spark.sql(
      """
    SELECT product_id, COUNT(*) as cnt
    FROM lake.default.user_behaviors
    GROUP BY product_id
    ORDER BY cnt DESC
    LIMIT 10
  """.stripMargin)

    // 可选：打印数据到控制台
    hotProducts.show()

    val jdbcUrl = "jdbc:mysql://localhost:3307/lakemart?useSSL=false&serverTimezone=Asia/Shanghai"
    val props = new Properties()
    props.setProperty("user", "root")
    props.setProperty("password", "root")
    props.setProperty("driver", "com.mysql.cj.jdbc.Driver")

    // 清空表
    import java.sql.{DriverManager, Statement}
    var conn: java.sql.Connection = null
    var stmt: Statement = null
    try {
      conn = DriverManager.getConnection(jdbcUrl, props)
      stmt = conn.createStatement()
      stmt.executeUpdate("TRUNCATE TABLE hot_products")
    } finally {
      if (stmt != null) stmt.close()
      if (conn != null) conn.close()
    }

    hotProducts.write.mode("append").jdbc(jdbcUrl, "hot_products", props)
    spark.stop()
  }
}