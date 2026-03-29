# Production Dockerfile for cch Spring Boot backend
# Build stage
FROM maven:3.10.1-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN mvn -B -DskipTests package

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/cch-0.0.1-SNAPSHOT.jar ./app.jar

# Allow overriding profile and env by docker run / compose
ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -jar /app/app.jar"]
