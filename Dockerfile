FROM maven:3.9.4-eclipse-temurin-17 AS build

#Configura el directorio de trabajo en el contenedor
WORKDIR /app

# Copia el archivo pom.xml y el directorio src al contenedor
COPY pom.xml ./
COPY src ./src

# Ejecuta la fase de empaquetado de Maven para generar el JAR
RUN mvn clean package -DskipTests

# Contenedor para ejecutar la aplicación
FROM openjdk:17-jdk-slim

# Configura el directorio de trabajo en el contenedor
WORKDIR /app

# Copia el archivo JAR generado en la fase de construcción
COPY --from=build /app/target/Login-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
