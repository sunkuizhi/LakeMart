package org.lzx.lakemart.task;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

@Component
@EnableScheduling
@Slf4j
public class SparkBatchScheduler {

    @Value("${spark.java17.path:java}")
    private String java17Path;

    @Value("${spark.fat.jar.path:D:/MyDemo/LakeMart/lakemart-spark/target/lakemart-spark-1.0-SNAPSHOT.jar}")
    private String fatJarPath;

    @PostConstruct
    public void init() {
        log.info("✅ SparkBatchScheduler 已初始化！定时任务已注册。");
    }
    /**
     * 用户画像计算：每天凌晨 2 点执行一次
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void runUserProfileBatch() {
        runSparkJob("org.lzx.lakemart.spark.batch.UserProfileBatch");
    }

    /**
     * 热销商品统计：每 5 分钟执行一次
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void runHotProductsBatch() {
        runSparkJob("org.lzx.lakemart.spark.batch.HotProductsBatch");
    }

    /**
     * 行为趋势统计：每 10 分钟执行一次
     */
    @Scheduled(cron = "0 */10 * * * ?")
    public void runBehaviorTrendBatch() {
        runSparkJob("org.lzx.lakemart.spark.batch.BehaviorTrendBatch");
    }

    /**
     * 行为分布统计：每 10 分钟执行一次
     */
    @Scheduled(cron = "0 */10 * * * ?")
    public void runActionDistributionBatch() {
        runSparkJob("org.lzx.lakemart.spark.batch.ActionDistributionBatch");
    }

    private void runSparkJob(String mainClass) {
        File jarFile = new File(fatJarPath);
        if (!jarFile.exists()) {
            log.error("Spark 胖 JAR 包不存在，请先执行 mvn package: {}", fatJarPath);
            return;
        }

        try {
            ProcessBuilder pb = new ProcessBuilder(
                    java17Path,
                    "--add-opens", "java.base/sun.nio.ch=ALL-UNNAMED",
                    "--add-opens", "java.base/java.nio=ALL-UNNAMED",
                    "--add-opens", "java.base/java.lang=ALL-UNNAMED",
                    "--add-opens", "java.base/jdk.internal.ref=ALL-UNNAMED",
                    "-Dio.netty.tryReflectionSetAccessible=true",
                    "-Dspark.ui.enabled=false",
                    "-cp", fatJarPath,
                    mainClass
            );

            pb.redirectErrorStream(true);
            log.info("正在启动 Spark 批处理作业: {}", mainClass);

            Process process = pb.start();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.info("[Spark-{}] {}", mainClass.substring(mainClass.lastIndexOf('.') + 1), line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                log.info("✅ Spark 批处理作业 [{}] 执行成功！", mainClass);
            } else {
                log.error("❌ Spark 批处理作业 [{}] 执行失败，退出码: {}", mainClass, exitCode);
            }

        } catch (Exception e) {
            log.error("执行 Spark 批处理作业 [{}] 异常: {}", mainClass, e.getMessage(), e);
        }
    }
}