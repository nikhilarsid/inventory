# Stage 1: Build the application
FROM gradle:jdk17-alpine AS build
WORKDIR /app
COPY . .
# Build the JAR
RUN ./gradlew clean build -x test --no-daemon

# Stage 2: Run the application
# ✅ FIX: Use 'eclipse-temurin' instead of the deprecated 'openjdk' image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copy the JAR from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the port
EXPOSE 8082

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]