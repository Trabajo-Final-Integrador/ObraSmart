FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/media-service-0.0.1-SNAPSHOT.jar app.jar
VOLUME /app/storage
ENV APP_STORAGE_BASE_PATH=/app/storage
ENTRYPOINT ["java","-jar","/app/app.jar","--app.storage.base-path=${APP_STORAGE_BASE_PATH}"]
