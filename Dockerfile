# syntax=docker/dockerfile:1.3
# Não funcionar basta rodar o comando : mvn -N io.takari:maven:wrapper
ARG BASE_IMAGE=eclipse-temurin:24-jdk

FROM ${BASE_IMAGE} AS build
WORKDIR /app

COPY mvnw ./
COPY .mvn .mvn
COPY pom.xml ./
RUN chmod +x mvnw

RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw dependency:go-offline -B

COPY src ./src
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw clean install -DskipTests

FROM ${BASE_IMAGE} AS runtime
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
