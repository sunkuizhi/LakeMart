// package org.lzx.lakemart.spark;
//
// import org.apache.kafka.clients.consumer.ConsumerRecord;
// import org.apache.spark.SparkConf;
// import org.apache.spark.streaming.Durations;
// import org.apache.spark.streaming.api.java.JavaInputDStream;
// import org.apache.spark.streaming.api.java.JavaStreamingContext;
// import org.apache.spark.streaming.kafka010.ConsumerStrategies;
// import org.apache.spark.streaming.kafka010.KafkaUtils;
// import org.apache.spark.streaming.kafka010.LocationStrategies;
//
// import java.util.Collections;
// import java.util.HashMap;
// import java.util.Map;
//
// public class SimpleConsumer {
//
//     public static void main(String[] args) throws InterruptedException {
//         // 1. Spark 配置
//         SparkConf conf = new SparkConf()
//                 .setAppName("SimpleConsumer")
//                 .setMaster("local[*]");   // 本地运行
//
//         // 2. 创建 StreamingContext，批次间隔 5 秒
//         JavaStreamingContext ssc = new JavaStreamingContext(conf, Durations.seconds(5));
//
//         // 3. Kafka 参数
//         Map<String, Object> kafkaParams = new HashMap<>();
//         kafkaParams.put("bootstrap.servers", "localhost:9092");
//         kafkaParams.put("group.id", "test-consumer-group");
//         kafkaParams.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//         kafkaParams.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//         kafkaParams.put("auto.offset.reset", "earliest");  // 从最早的消息开始读取
//         kafkaParams.put("enable.auto.commit", false);
//
//         // 4. 订阅 topic
//         JavaInputDStream<ConsumerRecord<String, String>> stream =
//                 KafkaUtils.createDirectStream(
//                         ssc,
//                         LocationStrategies.PreferConsistent(),
//                         ConsumerStrategies.<String, String>Subscribe(Collections.singleton("user-actions"), kafkaParams)
//                 );
//
//         // 5. 打印每批消息
//         stream.foreachRDD(rdd -> {
//             System.out.println("=== 收到批次，消息数量: " + rdd.count());
//             rdd.foreach(record -> {
//                 System.out.println("Kafka 消息: " + record.value());
//             });
//         });
//
//         // 6. 启动流计算
//         ssc.start();
//         ssc.awaitTermination();
//     }
// }