FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -e -B -DskipTests dependency:go-offline
COPY src src
RUN mvn -q -e -B package -DskipTests

FROM eclipse-temurin:17-jre-alpine
ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8
ENV JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF-8"
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar
VOLUME /app/storage
ENV APP_STORAGE_BASE_PATH=/app/storage
ENTRYPOINT ["java","-jar","/app/app.jar"]
