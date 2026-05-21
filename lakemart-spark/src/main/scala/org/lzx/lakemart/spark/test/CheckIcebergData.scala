package org.lzx.lakemart.spark.test

import org.apache.spark.sql.SparkSession

object CheckIcebergData {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("CheckIcebergData")
      .master("local[*]")
      .config("spark.sql.extensions", "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions")
      .config("spark.sql.catalog.lake", "org.apache.iceberg.spark.SparkCatalog")
      .config("spark.sql.catalog.lake.type", "hadoop")
      .config("spark.sql.catalog.lake.warehouse", "s3a://lakemart-warehouse/iceberg")
      .config("spark.hadoop.fs.s3a.endpoint", "http://localhost:9000")
      .config("spark.hadoop.fs.s3a.access.key", "minioadmin")
      .config("spark.hadoop.fs.s3a.secret.key", "minioadmin")
      .config("spark.hadoop.fs.s3a.path.style.access", "true")
      .getOrCreate()

    val df = spark.sql("SELECT * FROM lake.default.user_behaviors")
    df.show()
    spark.stop()
  }
}