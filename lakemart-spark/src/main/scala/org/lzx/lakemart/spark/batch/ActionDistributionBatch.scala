package org.lzx.lakemart.spark.batch

import org.apache.spark.sql.SparkSession
import java.util.Properties

object ActionDistributionBatch {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("ActionDistributionBatch")
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

    // 按 action 分组统计
    val distributionDF = spark.sql(
      """
        SELECT action, COUNT(*) as cnt
        FROM lake.default.user_behaviors
        GROUP BY action
        ORDER BY cnt DESC
      """.stripMargin)

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
      stmt.executeUpdate("TRUNCATE TABLE action_distribution")
    } finally {
      if (stmt != null) stmt.close()
      if (conn != null) conn.close()
    }

    // 写入新数据
    distributionDF.write.mode("append").jdbc(jdbcUrl, "action_distribution", props)

    spark.stop()
  }
}