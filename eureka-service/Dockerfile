# Usa una imagen base de Java
FROM openjdk:17-jdk-alpine
# Copia el jar generado por Maven
COPY target/eureka-service-0.0.1-SNAPSHOT.jar app.jar
# Expone el puerto (ajusta según tu microservicio)
EXPOSE 8761
# Comando para ejecutar la app
ENTRYPOINT ["java", "-jar", "/app.jar"]
