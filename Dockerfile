FROM maven:3.8.4-openjdk-17-slim AS build
#ARG JAR_FILE=*.jar
#COPY ./data /data/
#COPY target/${JAR_FILE} app.jar
#ENTRYPOINT ["java", "-jar", "app.jar"]

COPY ./ ./
ENTRYPOINT ["mvn", "spring-boot:run"]