# Etapa de construcción
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# Copia los archivos necesarios
COPY pom.xml ./
COPY src ./src

# Construye la aplicación sin ejecutar pruebas para agilizar
RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests

# Etapa de ejecución con Tomcat
FROM tomcat:10.1-jdk17
WORKDIR /usr/local/tomcat/webapps

# Copia la aplicación WAR en Tomcat
COPY --from=build /app/target/Login-0.0.1-SNAPSHOT.war ./ROOT.war

# Exponer puerto 8080
EXPOSE 8080

# Healthcheck para validar que Tomcat está en ejecución
HEALTHCHECK --interval=30s --timeout=10s --start-period=10s \
  CMD curl --fail http://localhost:8080/Login || exit 1

# Iniciar Tomcat
CMD ["catalina.sh", "run"]
