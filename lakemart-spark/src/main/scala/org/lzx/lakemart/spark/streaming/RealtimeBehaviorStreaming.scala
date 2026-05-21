package org.lzx.lakemart.spark.streaming

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming.Trigger
import java.io.File

object RealtimeBehaviorStreaming {
  def main(args: Array[String]): Unit = {
    // 在项目根目录下创建必要的本地目录
    val baseDir = System.getProperty("user.dir") + "/data/spark"
    val checkpointDir = baseDir + "/checkpoints"
    val bufferDir = baseDir + "/buffer"
    new File(checkpointDir).mkdirs()
    new File(bufferDir).mkdirs()

    val spark = SparkSession.builder()
      .appName("RealtimeBehaviorStreaming")
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
      .config("spark.hadoop.fs.s3a.buffer.dir", bufferDir)
      .getOrCreate()

    import spark.implicits._

    // 创建 Iceberg 表（如果不存在）
    spark.sql(
      """
        |CREATE TABLE IF NOT EXISTS lake.default.user_behaviors (
        |  user_id BIGINT,
        |  action STRING,
        |  product_id BIGINT,
        |  event_time TIMESTAMP
        |) USING iceberg
      """.stripMargin)

    val kafkaDF = spark.readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("subscribe", "user-behaviors")
      .option("startingOffsets", "latest")
      .load()
      .selectExpr("CAST(value AS STRING) as json")

    val behaviorDF = kafkaDF.select(
      get_json_object($"json", "$.userId").cast("bigint").as("user_id"),
      get_json_object($"json", "$.action").as("action"),
      get_json_object($"json", "$.productId").cast("bigint").as("product_id"),
      get_json_object($"json", "$.ts").cast("timestamp").as("event_time")
    ).filter($"user_id".isNotNull)

    val query = behaviorDF.writeStream
      .outputMode("append")
      .format("iceberg")
      .option("path", "lake.default.user_behaviors")
      .option("checkpointLocation", checkpointDir)
      .trigger(Trigger.ProcessingTime("10 seconds"))
      .start()

    query.awaitTermination()
  }
}