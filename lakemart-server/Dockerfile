# 使用 JDK 21 基础镜像
FROM eclipse-temurin:21-jdk
# 设置工作目录
WORKDIR /app

# 复制 jar 包（需要先通过 Maven 构建）
COPY target/lakemart-server-*.jar app.jar

# 暴露端口
EXPOSE 8080

# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"]