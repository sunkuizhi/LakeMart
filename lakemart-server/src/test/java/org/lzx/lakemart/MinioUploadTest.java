package org.lzx.lakemart;

import org.junit.jupiter.api.Test;
import org.lzx.lakemart.util.MinioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileInputStream;

@SpringBootTest
public class MinioUploadTest {

    @Autowired
    private MinioUtil minioUtil;

    @Test
    public void testUpload() throws Exception {
        // 替换为你的本地图片路径
        String path = "D:/test.png";
        try (FileInputStream input = new FileInputStream(path)) {
            MultipartFile file = new MockMultipartFile("file", "test.png", "image/png", input);
            String url = minioUtil.uploadFile(file, "product");
            System.out.println("上传成功: " + url);
        }
    }
}