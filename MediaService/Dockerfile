FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/*.jar /app/app.jar
VOLUME /app/storage
ENV APP_STORAGE_BASE_PATH=/app/storage
ENTRYPOINT ["java","-jar","/app/app.jar"]
