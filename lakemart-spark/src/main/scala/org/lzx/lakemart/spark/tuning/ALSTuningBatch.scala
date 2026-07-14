package org.lzx.lakemart.spark.tuning

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.ml.recommendation.ALS
import org.apache.spark.ml.recommendation.ALSModel
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.tuning.ParamGridBuilder
import org.apache.spark.ml.tuning.TrainValidationSplit

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * ALS 模型参数调优作业（Rank + RegParam 网格搜索）
 *
 * 运行方式：直接在 IDEA 中执行 main 方法（JDK 17 + 已启动 Docker 依赖服务）
 * 数据来源：Iceberg 表 lake.default.user_behaviors（需包含足够数据）
 *
 * 调优目标：找到使 RMSE 最小的 rank 和 regParam 组合
 */
object ALSTuningBatch {

  def main(args: Array[String]): Unit = {

    // ==================== 1. 初始化 Spark ====================
    val spark = SparkSession.builder()
      .appName("ALSTuningBatch")
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
      .config("spark.sql.shuffle.partitions", "4")
      .config("spark.ui.enabled", "false")
      .getOrCreate()

    import spark.implicits._

    println(s"==================== 调优开始: ${LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)} ====================")

    // ==================== 2. 从 Iceberg 加载行为数据 ====================
    val behaviorDF = spark.sql("""
      SELECT
        user_id,
        product_id,
        CASE action
          WHEN 'VIEW_PRODUCT' THEN 1.0
          WHEN 'ADD_CART'    THEN 3.0
          WHEN 'BUY'         THEN 5.0
          ELSE 0.0
        END as weight,
        event_time
      FROM lake.default.user_behaviors
      WHERE event_time >= date_sub(current_date(), 30)
        AND user_id IS NOT NULL
        AND product_id IS NOT NULL
    """).filter($"weight" > 0.0)

    val ratingDF = behaviorDF
      .groupBy($"user_id", $"product_id")
      .agg(sum($"weight").as("rating"))
      .filter($"rating" > 0.0)

    val totalRecords = ratingDF.count()
    println(s"加载的评分记录数: $totalRecords")

    if (totalRecords < 100) {
      println("⚠️ 数据量过少（< 100条），调优结果不具备参考价值，请先运行数据模拟器生成足够行为数据。")
      spark.stop()
      return
    }

    // ==================== 3. 拆分训练集 & 测试集 ====================
    val Array(train, test) = ratingDF.randomSplit(Array(0.8, 0.2), seed = 42L)

    // ==================== 4. 定义 ALS 模型 ====================
    val als = new ALS()
      .setUserCol("user_id")
      .setItemCol("product_id")
      .setRatingCol("rating")
      .setMaxIter(10)
      .setColdStartStrategy("drop")

    // ==================== 5. 构建参数网格 ====================
    val paramGrid = new ParamGridBuilder()
      .addGrid(als.rank, Array(5, 10, 20, 30, 50))
      .addGrid(als.regParam, Array(0.01, 0.05, 0.1, 0.5))
      .build()

    println(s"待测试参数组合数: ${paramGrid.length}")

    // ==================== 6. 使用 TrainValidationSplit 进行调优 ====================
    val evaluator = new RegressionEvaluator()
      .setMetricName("rmse")
      .setLabelCol("rating")
      .setPredictionCol("prediction")

    val tvs = new TrainValidationSplit()
      .setEstimator(als)
      .setEstimatorParamMaps(paramGrid)
      .setEvaluator(evaluator)
      .setTrainRatio(0.8)
      .setParallelism(2)

    // ==================== 7. 执行调优 ====================
    println("开始训练与验证... (预计耗时 3-10 分钟，取决于数据量)")
    val startTime = System.currentTimeMillis()
    val model = tvs.fit(train)
    val elapsed = (System.currentTimeMillis() - startTime) / 1000
    println(s"训练完成，耗时: ${elapsed}秒")

    // ==================== 8. 提取最佳参数 ====================
    val bestModel = model.bestModel.asInstanceOf[ALSModel]
    val bestRank = bestModel.rank

    // 从验证指标中找出最佳参数组合的索引
    val metrics = model.validationMetrics
    val bestIndex = metrics.zipWithIndex.minBy(_._1)._2
    val bestParams = paramGrid(bestIndex)
    val bestRegParam = bestParams(als.regParam)

    // 在测试集上评估最终 RMSE
    val testPredictions = bestModel.transform(test)
    val testRmse = evaluator.evaluate(testPredictions)

    // ==================== 9. 输出调优结果 ====================
    println(s"""
    ═══════════════════════════════════════════════════════════════════
                    🎯 ALS 参数调优结果（RMSE 越小越好）
    ═══════════════════════════════════════════════════════════════════
      最佳 Rank (隐因子数)      : ${bestRank}
      最佳 RegParam (正则化)    : ${bestRegParam}
      测试集 RMSE              : ${testRmse}
      全部参数组合数           : ${paramGrid.length}
      总耗时                   : ${elapsed} 秒
    ═══════════════════════════════════════════════════════════════════
    """)

    // ==================== 10. 打印所有组合的 RMSE 详情 ====================
    println("各参数组合的验证 RMSE 详情：")
    paramGrid.zip(metrics).foreach { case (params, metric) =>
      val rank = params(als.rank)
      val reg = params(als.regParam)
      println(f"  Rank = ${rank}%2d, RegParam = ${reg}%.2f  ->  验证 RMSE = ${metric}%.4f")
    }

    spark.stop()
    println("调优作业结束。")
  }
}