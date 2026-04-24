// package org.lzx.lakemart.controller;
//
// import org.lzx.lakemart.result.Result;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;
// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.nio.file.StandardCopyOption;
// import java.util.UUID;
//
// @RestController
// @RequestMapping("/api/temp")
// public class TempFileController {
//
//     @PostMapping("/upload")
//     public Result<String> uploadTempFile(@RequestParam("file") MultipartFile file) {
//         try {
//             String basePath = System.getProperty("user.dir");
//             Path tempDir = Paths.get(basePath, "temp");
//             if (!Files.exists(tempDir)) {
//                 Files.createDirectories(tempDir);
//             }
//             String originalFilename = file.getOriginalFilename();
//             String ext = "";
//             if (originalFilename != null && originalFilename.contains(".")) {
//                 ext = originalFilename.substring(originalFilename.lastIndexOf("."));
//             }
//             String fileName = UUID.randomUUID().toString() + ext;
//             Path targetPath = tempDir.resolve(fileName);
//             Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
//             return Result.success(fileName);
//         } catch (IOException e) {
//             e.printStackTrace();
//             return Result.error("临时文件上传失败: " + e.getMessage());
//         }
//     }
// }