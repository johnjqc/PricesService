# Build stage
FROM gradle:8.5-jdk21 AS build

WORKDIR /app

# Copy build files first for better caching
COPY build.gradle settings.gradle ./
COPY domain/build.gradle ./domain/
COPY application/build.gradle ./application/
COPY adapters/build.gradle ./adapters/
COPY adapters/rest-adapter/build.gradle ./adapters/rest-adapter/
COPY adapters/persistence-adapter/build.gradle ./adapters/persistence-adapter/
COPY bootstrap/build.gradle ./bootstrap/

# Copy source code
COPY domain/src ./domain/src
COPY application/src ./application/src
COPY adapters/rest-adapter/src ./adapters/rest-adapter/src
COPY adapters/persistence-adapter/src ./adapters/persistence-adapter/src
COPY bootstrap/src ./bootstrap/src

# Build the application
RUN gradle :bootstrap:bootJar --no-daemon

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy the built jar from build stage
COPY --from=build /app/bootstrap/build/libs/*.jar app.jar

# Set ownership
RUN chown -R appuser:appgroup /app

USER appuser

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]