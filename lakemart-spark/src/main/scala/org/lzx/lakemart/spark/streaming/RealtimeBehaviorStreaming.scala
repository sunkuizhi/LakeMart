package org.lzx.lakemart.spark.streaming

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming.{OutputMode, Trigger}
import redis.clients.jedis.JedisPool
import java.io.File
import org.apache.spark.sql.types._

import java.util.Properties

object RealtimeBehaviorStreaming {
  def main(args: Array[String]): Unit = {
    // 创建本地 checkpoint 目录
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

    // 1️⃣ 创建 Iceberg 表（如果不存在）
    spark.sql(
      """
        |CREATE TABLE IF NOT EXISTS lake.default.user_behaviors (
        |  user_id BIGINT,
        |  action STRING,
        |  product_id BIGINT,
        |  event_time TIMESTAMP
        |) USING iceberg
      """.stripMargin)

    // 2️⃣ 定义 JSON 结构
    val schema = StructType(Seq(
      StructField("userId", LongType, true),
      StructField("action", StringType, true),
      StructField("productId", LongType, true),
      StructField("ts", StringType, true)
    ))

    // 3️⃣ 从 Kafka 读取数据
    val kafkaDF = spark.readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("subscribe", "user-behaviors")
      .option("startingOffsets", "latest")
      .load()
      .selectExpr("CAST(value AS STRING) as json_raw")
      .withColumn("json_clean", regexp_replace($"json_raw", "^\"|\"$", ""))
      .withColumn("json_clean", regexp_replace($"json_clean", "\\\\\"", "\""))
//
//    // 调试输出（可选）
//    kafkaDF.select($"json_clean")
//      .writeStream
//      .outputMode("append")
//      .format("csv")
//      .option("path", "D:/MyDemo/LakeMart/debug_json_clean")
//      .option("header", "true")
//      .option("checkpointLocation", "D:/MyDemo/LakeMart/debug_json_clean_checkpoint")
//      .trigger(Trigger.ProcessingTime("5 seconds"))
//      .start()

    // 4️⃣ 解析 JSON
    val behaviorDF = kafkaDF
      .select(from_json($"json_clean", schema).as("data"))
      .select(
        $"data.userId".as("user_id"),
        $"data.action".as("action"),
        $"data.productId".as("product_id"),
        to_timestamp($"data.ts", "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS").as("event_time")
      )
      .filter($"user_id".isNotNull)
      .filter($"event_time".isNotNull)

    // 调试输出（可选）
//    behaviorDF.writeStream
//      .outputMode("append")
//      .format("csv")
//      .option("path", "D:/MyDemo/LakeMart/debug_parsed")
//      .option("header", "true")
//      .option("checkpointLocation", "D:/MyDemo/LakeMart/debug_parsed_checkpoint")
//      .trigger(Trigger.ProcessingTime("5 seconds"))
//      .start()

    // 6️⃣ 写入 MySQL（用于前端实时监控）
    val mysqlQuery = behaviorDF.writeStream
      .outputMode("append")
      .foreachBatch { (batchDF: org.apache.spark.sql.DataFrame, batchId: Long) =>  // ✅ 修复：显式声明类型
        if (!batchDF.isEmpty) {
          val url = "jdbc:mysql://localhost:3307/lakemart?useSSL=false&serverTimezone=Asia/Shanghai"
          val props = new Properties()
          props.setProperty("user", "root")
          props.setProperty("password", "root")
          props.setProperty("driver", "com.mysql.cj.jdbc.Driver")

          val mysqlDF = batchDF.select(
            $"user_id",
            $"action",
            $"product_id",
            $"event_time".as("create_time")
          )

          mysqlDF.write
            .mode("append")
            .jdbc(url, "user_behavior_log", props)

          println(s"✅ 写入 MySQL 成功，批次 $batchId，共 ${mysqlDF.count()} 条")
        }
      }
      .trigger(Trigger.ProcessingTime("5 seconds"))
      .start()

    // 7️⃣ 窗口聚合
    val windowedAgg = behaviorDF
      .withWatermark("event_time", "1 minute")
      .groupBy(
        window($"event_time", "1 minute", "10 seconds"),
        $"product_id"
      )
      .agg(count("*").as("cnt"))

    // 8️⃣ 写入 Iceberg
    val icebergQuery = behaviorDF.writeStream
      .outputMode("append")
      .format("iceberg")
      .option("path", "lake.default.user_behaviors")
      .option("checkpointLocation", checkpointDir + "/iceberg")
      .trigger(Trigger.ProcessingTime("10 seconds"))
      .start()

    // 9️⃣ 写入 Redis ZSET
    val redisQuery = windowedAgg.writeStream
      .outputMode(OutputMode.Update())
      .foreachBatch(new org.apache.spark.api.java.function.VoidFunction2[org.apache.spark.sql.Dataset[org.apache.spark.sql.Row], java.lang.Long] {
        override def call(batchDF: org.apache.spark.sql.Dataset[org.apache.spark.sql.Row], batchId: java.lang.Long): Unit = {
          val results = batchDF
            .groupBy("product_id")
            .agg(sum("cnt").as("totalCnt"))
            .collect()

          if (results.nonEmpty) {
            val jedisPool = new JedisPool("localhost", 6379)
            val jedis = jedisPool.getResource
            try {
              results.foreach { row =>
                val productId = row.getLong(0)
                val cnt = row.getLong(1)
                jedis.zincrby("hot:products", cnt, productId.toString)
              }
              jedis.zremrangeByRank("hot:products", 0, -101)
            } finally {
              jedis.close()
              jedisPool.close()
            }
          }
        }
      })
      .trigger(Trigger.ProcessingTime("10 seconds"))
      .start()

    // 🔟 等待所有流结束
    icebergQuery.awaitTermination()
    redisQuery.awaitTermination()
    mysqlQuery.awaitTermination()
  }
}