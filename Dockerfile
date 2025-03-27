# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /workspace/app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Extrai o JAR corretamente (sem erros de digitação)
RUN mkdir -p target/extracted && \
    (cd target/extracted && jar -xf ../*.jar)

# Runtime stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /workspace/app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
EXPOSE 8080