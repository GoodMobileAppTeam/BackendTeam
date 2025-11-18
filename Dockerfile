# ============================
# Dockerfile.local (로컬 개발용)
# ============================

# Base image
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# 로컬 또는 CI에서 빌드된 jar 파일만 복사
COPY build/libs/*-SNAPSHOT.jar app.jar

# Expose application port
EXPOSE 8080

# 기본 active profile = local
ENTRYPOINT ["java", "-jar", "app/app.jar"]
