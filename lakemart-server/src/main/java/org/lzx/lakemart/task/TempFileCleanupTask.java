// package org.lzx.lakemart.task;
//
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.scheduling.annotation.Scheduled;
// import org.springframework.stereotype.Component;
// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.nio.file.attribute.BasicFileAttributes;
// import java.time.Instant;
// import java.time.temporal.ChronoUnit;
//
// @Component
// public class TempFileCleanupTask {
//
//     private String tempPath = System.getProperty("user.dir") + "/temp";
//
//     /**
//      * 每小时执行一次，删除超过1小时的临时文件
//      */
//     @Scheduled(fixedRate = 3600000)
//     public void cleanOldFiles() {
//         try {
//             Path tempDir = Paths.get(tempPath);
//             if (!Files.exists(tempDir)) {
//                 return;
//             }
//             Files.list(tempDir).forEach(file -> {
//                 try {
//                     BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);
//                     if (attrs.creationTime().toInstant().isBefore(Instant.now().minus(1, ChronoUnit.HOURS))) {
//                         Files.deleteIfExists(file);
//                     }
//                 } catch (IOException ignored) {
//                 }
//             });
//         } catch (Exception ignored) {
//         }
//     }
// }