FROM openjdk:17.0.1-jdk-slim

COPY target/e-commerce-0.0.1-SNAPSHOT.jar /e-commerce.jar

# Expose the port that your Spring Boot application listens on
EXPOSE 8080

# CONNECTING TO UPLOAD VOLUME
VOLUME ["/app/uploads"]

# Define the command to run your Spring Boot application
ENTRYPOINT ["java", "-jar", "e-commerce.jar"]



