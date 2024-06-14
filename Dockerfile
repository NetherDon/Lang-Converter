FROM maven:3.8.4-openjdk-17-slim AS build

COPY ./ ./
ENTRYPOINT ["mvn", "spring-boot:run"]