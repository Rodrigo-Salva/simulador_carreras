FROM openjdk:21-jdk-slim

LABEL maintainer="TECSUP Vocacional Team"
LABEL service="usuarios-tokens-service"
LABEL version="1.0.0"

WORKDIR /app

# Instalar herramientas necesarias para producción
RUN apt-get update && apt-get install -y \
    wget \
    curl \
    dumb-init \
    && rm -rf /var/lib/apt/lists/*

# Copiar POM padre primero
COPY pom.xml ../pom.xml
COPY mvnw ../mvnw
COPY .mvn ../.mvn

# Instalar POM padre
RUN chmod +x ../mvnw && cd .. && ./mvnw install -N -DskipTests

# Copiar archivos del microservicio
COPY microservices/usuarios-tokens/pom.xml .
COPY microservices/usuarios-tokens/mvnw .
COPY microservices/usuarios-tokens/.mvn .mvn
COPY microservices/usuarios-tokens/src ./src

# Construir aplicación
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline -B
RUN ./mvnw clean package -DskipTests

# Crear usuario no-root para seguridad
RUN useradd -r -s /bin/false -u 1001 tecsup
RUN chown -R tecsup:tecsup /app
USER tecsup

EXPOSE 8081

# Variables de entorno optimizadas para producción
ENV SPRING_PROFILES_ACTIVE=docker
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+UseG1GC -Djava.security.egd=file:/dev/./urandom"

# Health check para monitoreo
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8081/api/auth/health || exit 1

# Usar dumb-init para manejo correcto de señales
ENTRYPOINT ["dumb-init", "--"]
CMD ["sh", "-c", "java $JAVA_OPTS -jar target/usuarios-tokens-1.0.0.jar"]
