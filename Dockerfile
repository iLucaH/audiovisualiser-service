FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# Copy Maven configuration first
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Make Maven wrapper executable
RUN chmod +x mvnw

# Download dependencies first
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build application
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the generated Spring Boot JAR
COPY --from=build /app/target/*.jar app.jar

# Spring Boot default port
EXPOSE 8080

# Start application
ENTRYPOINT ["java", "-jar", "app.jar"]