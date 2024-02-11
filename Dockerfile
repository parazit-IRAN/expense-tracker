# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17-slim AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean install

# Stage 2: Run the application
FROM openjdk:17-oracle

WORKDIR /app

EXPOSE 8484

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar ./app.jar
RUN ls /app
# Command to run the application when the container starts
CMD ["java", "-jar", "/app/app.jar", "-Dspring.profiles.active=dev"]
# Removed the unnecessary "expense-tracker" argument
