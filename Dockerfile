# Stage 1: Build the Spring Boot application using Maven
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copy the Maven project definition and build files
COPY pom.xml .
COPY src ./src

# Build the application, skipping tests
RUN mvn clean package -DskipTests

# Stage 2: Create the final image with the built JAR file
FROM openjdk:17.0.1-jdk-slim
WORKDIR /app

# Copy the JAR file from the build stage to this stage
COPY --from=build /app/target/e-commerce-0.0.1-SNAPSHOT.jar e-commerce.jar

# Expose the port that your Spring Boot application listens on
EXPOSE 8080

# Define the command to run your Spring Boot application
ENTRYPOINT ["java", "-jar", "e-commerce.jar"]
