# Stage 1: Build the application using Gradle with JDK 17
FROM gradle:jdk17-alpine AS build
WORKDIR /app
COPY . .
# Build the JAR
RUN ./gradlew clean build -x test --no-daemon

# Stage 2: Run the application using OpenJDK 17
FROM openjdk:17-jdk-slim
WORKDIR /app
# Copy the JAR from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the port (Change this to 8082 for Inventory, 8060 for Auth, etc.)
EXPOSE 8082

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]