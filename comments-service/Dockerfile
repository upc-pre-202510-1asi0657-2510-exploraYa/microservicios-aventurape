# Usa una imagen base de Java
FROM openjdk:17-jdk-alpine
# Copia el jar generado por Maven
COPY target/comments-service-0.0.1-SNAPSHOT.jar app.jar
# Expone el puerto correcto para comments-service
EXPOSE 8084
# Comando para ejecutar la app
ENTRYPOINT ["java", "-jar", "/app.jar"]