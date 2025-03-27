# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /workspace/app
COPY pom.xml .
# Cache das dependências
RUN mvn dependency:go-offline -B

COPY src ./src
# Build com otimizações
RUN mvn clean package -DskipTests -Dmaven.test.skip=true -T 1C

# Runtime stage
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app
# Copia apenas o JAR necessário
COPY --from=builder /workspace/app/target/*.jar app.jar

# Configurações de segurança e performance
RUN apt-get update && \
    apt-get install -y --no-install-recommends dumb-init && \
    rm -rf /var/lib/apt/lists/*

# Usuário não-root para segurança
RUN adduser --system --group appuser && \
    chown appuser:appuser /app/app.jar

USER appuser

# Otimizações JVM
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+ExitOnOutOfMemoryError"

ENTRYPOINT ["dumb-init", "java", "-jar", "/app/app.jar"]
EXPOSE 8080
