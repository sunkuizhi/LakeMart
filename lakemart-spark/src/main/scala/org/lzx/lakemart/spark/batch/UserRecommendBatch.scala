package org.lzx.lakemart.spark.batch

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.ml.recommendation.ALS
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 离线用户推荐列表生成作业（带时间衰减）
 *
 * 功能：
 * 1. 从 Iceberg 表读取近30天用户行为，行为权重乘以时间衰减因子 exp(-0.1 * 天数差)
 * 2. 使用调优后的最佳参数（rank=30, regParam=0.5）训练 ALS 模型
 * 3. 为每个活跃用户生成 Top-20 推荐商品列表
 * 4. 将推荐列表写入 Redis（key: recommend:user:{userId}，value: JSON 数组）
 *
 * 调度：建议每天凌晨 2 点执行一次
 */
object UserRecommendBatch {

  private val REDIS_HOST = "localhost"
  private val REDIS_PORT = 6379
  private val REDIS_TIMEOUT = 2000
  private val REDIS_POOL_CONFIG = {
    val config = new JedisPoolConfig()
    config.setMaxTotal(10)
    config.setMaxIdle(5)
    config.setMinIdle(1)
    config.setTestOnBorrow(true)
    config
  }
  private lazy val jedisPool = new JedisPool(REDIS_POOL_CONFIG, REDIS_HOST, REDIS_PORT, REDIS_TIMEOUT)

  private val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)

  private val RANK = 30
  private val REG_PARAM = 0.5
  private val MAX_ITER = 10
  private val TOP_N = 20
  private val MIN_USER_BEHAVIORS = 5

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("UserRecommendBatch")
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
      .config("spark.sql.shuffle.partitions", "8")
      .config("spark.ui.enabled", "false")
      .getOrCreate()

    import spark.implicits._

    println(s"==================== 用户推荐列表生成开始（带时间衰减）: ${LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)} ====================")

    // ===== 1. 加载行为数据，应用时间衰减 =====
    val behaviorDF = spark.sql("""
      SELECT
        user_id,
        product_id,
        CASE action
          WHEN 'VIEW_PRODUCT' THEN 1.0 * exp(-0.1 * datediff(current_date(), event_time))
          WHEN 'ADD_CART'    THEN 3.0 * exp(-0.1 * datediff(current_date(), event_time))
          WHEN 'BUY'         THEN 5.0 * exp(-0.1 * datediff(current_date(), event_time))
          ELSE 0.0
        END as weight,
        event_time
      FROM lake.default.user_behaviors
      WHERE event_time >= date_sub(current_date(), 30)
        AND user_id IS NOT NULL
        AND product_id IS NOT NULL
    """).filter($"weight" > 0.0)

    // ===== 2. 聚合为评分（用户-商品评分） =====
    val ratingDF = behaviorDF
      .groupBy($"user_id", $"product_id")
      .agg(sum($"weight").as("rating"))
      .filter($"rating" > 0.0)

    // 过滤掉行为过少的用户（防止冷启动）
    val userBehaviorCount = ratingDF.groupBy("user_id").count().filter($"count" >= MIN_USER_BEHAVIORS)
    val filteredRatingDF = ratingDF.join(userBehaviorCount, Seq("user_id"), "inner")
      .select(ratingDF("user_id"), ratingDF("product_id"), ratingDF("rating"))

    val totalRatings = filteredRatingDF.count()
    println(s"参与训练的评分记录数: $totalRatings")

    if (totalRatings == 0) {
      println("⚠️ 没有足够的评分数据，跳过推荐生成。")
      spark.stop()
      return
    }

    // ===== 3. 训练 ALS 模型 =====
    val als = new ALS()
      .setRank(RANK)
      .setRegParam(REG_PARAM)
      .setMaxIter(MAX_ITER)
      .setUserCol("user_id")
      .setItemCol("product_id")
      .setRatingCol("rating")
      .setColdStartStrategy("drop")

    println("开始训练 ALS 模型...")
    val model = als.fit(filteredRatingDF)
    println("模型训练完成。")

    // ===== 4. 为所有用户生成推荐 =====
    val allUsers = filteredRatingDF.select("user_id").distinct()
    val recommendations = model.recommendForUserSubset(allUsers, TOP_N)

    // ===== 5. 将推荐结果写入 Redis =====
    println("正在将推荐结果写入 Redis...")
    val result = recommendations
      .select($"user_id", $"recommendations.product_id".as("product_ids"))
      .as[(Long, Array[Int])]
      .collect()

    val jedis = jedisPool.getResource
    try {
      result.foreach { case (userId, productIds) =>
        val key = s"recommend:user:$userId"
        val json = mapper.writeValueAsString(productIds)
        jedis.setex(key, 3600 * 24, json) // 缓存24小时
      }
      println(s"成功写入 ${result.length} 个用户的推荐列表到 Redis")
    } finally {
      if (jedis != null) jedis.close()
    }

    spark.stop()
    println("用户推荐列表生成作业结束。")
  }
}