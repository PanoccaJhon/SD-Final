FROM openjdk:17-jdk-alpine
LABEL authors="epis-unsa-sd"
ARG JAR_FILE=target/*.jar
COPY ./target/account-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar","/app.jar"]