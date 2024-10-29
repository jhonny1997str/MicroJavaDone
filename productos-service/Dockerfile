# productos-service/Dockerfile

# Etapa de construcción
FROM maven:3.9.6-eclipse-temurin-22-jammy AS build

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo pom.xml en el directorio de trabajo del contenedor
COPY pom.xml .

# Copia el directorio 'src' en el contenedor, que contiene el código fuente de la aplicación
COPY src ./src

# Ejecuta el comando Maven para limpiar y empaquetar la aplicación
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM openjdk:21-jdk-slim

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo JAR generado en la etapa de construcción y lo renombra a app.jar
COPY --from=build /app/target/productos-service-0.0.1-SNAPSHOT.jar app.jar

# Comando de entrada que se ejecutará cuando se inicie el contenedor
ENTRYPOINT ["java", "-jar", "app.jar"]
