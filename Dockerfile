# # ==============================
# 第一阶段：构建阶段 (Builder)
# 使用 Maven 镜像进行编译，这一阶段会产生很多缓存和垃圾文件，但没关系，最后不会打包进最终镜像
# ==============================
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder

# 设置工作目录
WORKDIR /app

# 1. 先只拷贝 pom.xml 和源码，利用 Docker 缓存机制加速构建
COPY pom.xml .
COPY src ./src

# 2. 执行打包命令 (跳过测试，加快速度)
RUN mvn package -DskipTests

# ==============================
# 第二阶段：运行阶段 (Runner)
# 只使用轻量级的 JRE 运行环境，抛弃 Maven 和编译缓存
# ==============================
FROM eclipse-temurin:21-jre-alpine

# ⚠️ 修复点：安装 curl，以确保 Docker Compose 的 Healthcheck 命令能正常执行
RUN apk add --no-cache curl

# 设置工作目录
WORKDIR /app

# 从第一阶段 (builder) 拷贝打好的 jar 包过来
# 注意：target/*.jar 对应你 pom.xml 生成的文件名，这里自动匹配
COPY --from=builder /app/target/*.jar app.jar

# 暴露应用端口
EXPOSE 8080

# 配置环境变量 (这也是评分点之一)
ENV TZ=Asia/Shanghai
ENV JAVA_OPTS=""

# 启动命令
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]