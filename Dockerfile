# =======================================================
# MyPetStore — 多阶段构建
# 用法: docker build -t mypetstore .
# =======================================================

# 阶段1: Maven 构建后端
FROM maven:3.9-eclipse-temurin-17 AS backend-builder
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests -q

# 阶段2: Node 构建前端
FROM node:22-alpine AS frontend-builder
WORKDIR /build
COPY frontend/package.json frontend/package-lock.json ./
RUN npm install --silent 2>/dev/null
COPY frontend/ ./
RUN npm run build

# 阶段3: 最终运行镜像（只放后端 jar）
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=backend-builder /build/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
