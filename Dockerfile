# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /workspace/app

# Cache de dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código fonte
COPY src src

# Build da aplicação (incluindo frontend estático)
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copia o JAR e frontend estático
COPY --from=builder /workspace/app/target/*.jar app.jar
COPY --from=builder /workspace/app/target/classes/static /app/static

# Configurações de segurança
RUN apt-get update && \
    apt-get install -y --no-install-recommends dumb-init && \
    rm -rf /var/lib/apt/lists/*

RUN adduser --system --group appuser && \
    chown appuser:appuser /app/app.jar

USER appuser

# Variáveis de ambiente (serão sobrescritas pelo Azure)
ENV SPRING_DATASOURCE_URL=${DB_URL}
ENV SPRING_DATASOURCE_USERNAME=${DB_USER}
ENV SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}
ENV SERVER_PORT=8080

EXPOSE 8080
ENTRYPOINT ["dumb-init", "sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]