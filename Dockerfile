# Multi-stage build

# Stage 1: Build
FROM maven:3.9-eclipse-temurin-23 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:23-jre-alpine
WORKDIR /app
COPY --from=build /app/target/Doorway-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080

# Accept profile as environment variable, default to 'docker'
ENV SPRING_PROFILES_ACTIVE=docker

ENTRYPOINT ["java", "-jar", "app.jar"]