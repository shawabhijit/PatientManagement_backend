FROM gradle:8.14.3-jdk21 AS builder

WORKDIR /app

# Copy Gradle files first for better layer caching
COPY build.gradle settings.gradle ./
COPY gradle gradle
COPY gradlew .

# Download dependencies
RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon

# Copy source code
COPY src src

# Build the application
RUN ./gradlew clean bootJar --no-daemon

# Runtime image
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8002
EXPOSE 9001

ENTRYPOINT ["java", "-jar", "app.jar"]