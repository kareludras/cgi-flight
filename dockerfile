# Frontend
FROM node:18-alpine AS frontend-build
WORKDIR /app
COPY flight-frontend/package*.json ./
RUN npm install
COPY flight-frontend/ ./
RUN npm run build

# Backend
FROM maven:3.9-eclipse-temurin-17-alpine AS backend-build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Final
FROM eclipse-temurin:23-jdk-alpine
WORKDIR /app
COPY --from=backend-build /app/target/*.jar app.jar
COPY --from=frontend-build /app/build ./public
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]