package org.lzx.lakemart;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import java.util.Collections;

public class CodeGenerator {
    public static void main(String[] args) {
        // 数据库连接信息（请改成你自己的密码）
        String url = "jdbc:mysql://localhost:3306/lakemart?useSSL=false&serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "sunkuizhi0128";   // ← 改成你实际的密码

        // String projectPath = System.getProperty("user.dir");
        String projectPath = "D:/MyDemo/LakeMart/lakemart-server";
        String javaPath = projectPath + "/src/main/java";
        String mapperXmlPath = projectPath + "/src/main/resources/mapper";

        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> builder
                        .author("lzx")
                        .outputDir(javaPath)
                        .commentDate("yyyy-MM-dd")
                        .disableOpenDir()
                )
                .packageConfig(builder -> builder
                        .parent("org.lzx.lakemart")
                        .entity("model.entity")
                        .mapper("mapper")
                        .service("service")
                        .serviceImpl("service.impl")
                        .xml("mapper.xml")
                        .pathInfo(Collections.singletonMap(OutputFile.xml, mapperXmlPath))
                )
                .strategyConfig(builder -> builder
                        .addTablePrefix("tb_")
                        // .addInclude("tb_user", "tb_category", "tb_product", "tb_cart_item", "tb_order", "tb_order_item")
                        // .addInclude("tb_address")
                        // .addInclude("tb_order ")
                        .addInclude("tb_points_log")
                        .entityBuilder()
                        .enableLombok()
                        .enableTableFieldAnnotation()
                        .mapperBuilder()
                        .enableMapperAnnotation()
                        .serviceBuilder()
                        .formatServiceFileName("%sService")
                )
                .templateEngine(new VelocityTemplateEngine())
                .execute();
    }
}