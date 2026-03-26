# ---------- Build stage ----------
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy pom first to leverage Docker layer caching for dependencies.
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

# Copy source and build application jar.
COPY src ./src
RUN mvn -B -DskipTests clean package

# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

# application.yaml uses server.port=8081
EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]