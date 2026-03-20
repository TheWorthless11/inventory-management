# Use official Java image
FROM eclipse-temurin:21-jdk-jammy

# Set working directory
WORKDIR /app

# Copy jar file (you MUST build it first)
COPY target/*.jar app.jar

# Expose port (Spring Boot default)
EXPOSE 8080

# Run app
ENTRYPOINT ["java", "-jar", "app.jar"]