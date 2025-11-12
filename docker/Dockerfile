##Base image
#FROM eclipse-temurin:21-jdk
#
##Set working directory
#WORKDIR /app
#
##Copy source code
#COPY . /app
#RUN rm -f /app/.env /app/*.env
#
##Grant execution permission for Gradle
#RUN chmod +x ./gradlew
#
##Remove existing JAR files and build application using bootJar with AWS profile
#RUN rm -rf build/libs/*.jar && \
#    SPRING_PROFILES_ACTIVE=aws ./gradlew clean bootJar -x test --stacktrace --info || \
#    (echo "Gradle bootJar build failed!" && exit 1)
#
##확인용 - 빌드된 JAR 파일 목록 출력
#RUN ls -lh build/libs/
#
##Find the generated bootJar file dynamically and copy it to /app.jar
#RUN JAR_FILE=$(ls build/libs/*-SNAPSHOT.jar | grep -v plain | tail -n 1) && cp "$JAR_FILE" /app.jar
#
##Expose application port
#EXPOSE 8080
#
##Run the application with AWS profile
#CMD ["java", "-Dspring.profiles.active=aws", "-jar", "/app.jar"]

# ✅ Base image
FROM eclipse-temurin:21-jdk

# ✅ Set working directory
WORKDIR /app

# ✅ Copy only built jar file from host (not source code)
# (이 jar는 GitHub Actions에서 ./gradlew bootJar -x test 후 생성된 파일)
COPY build/libs/*-SNAPSHOT.jar app.jar

# ✅ Expose application port
EXPOSE 8080

# ✅ Run the application with AWS profile
ENTRYPOINT ["java", "-Dspring.profiles.active=aws", "-jar", "app.jar"]

