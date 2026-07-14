package org.lzx.lakemart.spark.tools

import org.apache.spark.sql.SparkSession

object AlterIcebergTable {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("AlterIcebergTable")
      .master("local[*]")
      .config("spark.sql.extensions", "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions")
      .config("spark.sql.catalog.lake", "org.apache.iceberg.spark.SparkCatalog")
      .config("spark.sql.catalog.lake.type", "hadoop")
      .config("spark.sql.catalog.lake.warehouse", "s3a://lakemart-warehouse/iceberg")
      .config("spark.hadoop.fs.s3a.endpoint", "http://localhost:9000")
      .config("spark.hadoop.fs.s3a.access.key", "minioadmin")
      .config("spark.hadoop.fs.s3a.secret.key", "minioadmin")
      .config("spark.hadoop.fs.s3a.path.style.access", "true")
      .config("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
      // ===== 关键修复 =====
      .config("spark.hadoop.fs.s3a.buffer.dir", "D:/MyDemo/LakeMart/tmp")
      .config("spark.hadoop.hadoop.tmp.dir", "D:/MyDemo/LakeMart/tmp")
      .config("spark.local.dir", "D:/MyDemo/LakeMart/tmp")
      // ===== 其他配置 =====
      .config("spark.ui.enabled", "false")
      .getOrCreate()

    try {
      println("正在为 Iceberg 表增加 experiment_id 列...")
      spark.sql("ALTER TABLE lake.default.user_behaviors ADD COLUMN experiment_id STRING")
      println("✅ 成功增加列 experiment_id")
    } catch {
      case e: Exception =>
        if (e.getMessage.contains("already exists")) {
          println("⚠️ 列 experiment_id 已存在，无需重复添加")
        } else {
          println("❌ 执行失败：")
          e.printStackTrace()
        }
    } finally {
      spark.stop()
    }
  }
}