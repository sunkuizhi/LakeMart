package org.lzx.lakemart.util;
// package org.lzx.lakemart.util;
//
// import io.minio.BucketExistsArgs;
// import io.minio.MakeBucketArgs;
// import io.minio.MinioClient;
// import io.minio.PutObjectArgs;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Component;
// import org.springframework.web.multipart.MultipartFile;
// import java.io.InputStream;
// import java.util.UUID;
//
// @Component
// public class MinioUtil {
//
//     @Autowired
//     private MinioClient minioClient;
//
//     @Value("${minio.bucketName}")
//     private String bucketName;
//
//     /**
//      * 上传文件到 MinIO，返回文件访问路径
//      * @param file 上传的文件
//      * @param folder 存储目录（如 "product"）
//      * @return 文件 URL
//      */
//     public String uploadFile(MultipartFile file, String folder) {
//         try {
//             // 确保 bucket 存在
//             boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
//             if (!found) {
//                 minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
//                 // 可选：设置 bucket 为公开读（便于测试），生产环境建议使用预签名URL
//                 // 这里不设置，需要手动在控制台设置或使用预签名
//             }
//             // 生成唯一文件名
//             String originalFilename = file.getOriginalFilename();
//             String ext = "";
//             if (originalFilename != null && originalFilename.contains(".")) {
//                 ext = originalFilename.substring(originalFilename.lastIndexOf("."));
//             }
//             String fileName = folder + "/" + UUID.randomUUID() + ext;
//             InputStream inputStream = file.getInputStream();
//             minioClient.putObject(PutObjectArgs.builder()
//                     .bucket(bucketName)
//                     .object(fileName)
//                     .stream(inputStream, file.getSize(), -1)
//                     .contentType(file.getContentType())
//                     .build());
//             // 返回访问 URL（MinIO 默认端口 9000）
//             return String.format("http://localhost:9000/%s/%s", bucketName, fileName);
//         } catch (Exception e) {
//             throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
//         }
//     }
// }


import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.UUID;

@Component
public class MinioUtil {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucketName}")
    private String bucketName;

    /**
     * 上传文件到 MinIO，支持指定子目录
     * @param file 文件
     * @param subDir 子目录（如 "product", "banner", "avatar"）
     * @return 文件访问 URL
     */
    public String uploadFile(MultipartFile file, String subDir) {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            String originalFilename = file.getOriginalFilename();
            String ext = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                ext = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String fileName = subDir + "/" + UUID.randomUUID() + ext;
            InputStream inputStream = file.getInputStream();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
            return String.format("http://localhost:9000/%s/%s", bucketName, fileName);
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
        }
    }
}