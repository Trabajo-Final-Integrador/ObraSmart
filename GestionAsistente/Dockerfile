FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/GestionAsistente-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8099

ENTRYPOINT ["java", "-jar", "app.jar"]
