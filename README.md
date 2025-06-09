# Microservicios Aventurape

## Arquitectura

Este proyecto está basado en una arquitectura de microservicios, donde cada servicio tiene responsabilidades específicas:

- **config-service**: Servidor de configuración centralizada para todos los servicios
- **eureka-service**: Registro y descubrimiento de servicios
- **gateway-service**: Puerta de enlace API
- **iam-service**: Servicio de identidad y acceso (autenticación)
- **post-service**: Servicio de publicaciones
- **profile-service**: Servicio de perfiles de usuario
- **favorites-service**: Servicio de favoritos

## Configuración Centralizada

La aplicación utiliza Spring Cloud Config para la gestión centralizada de configuraciones. Todos los archivos de configuración se encuentran en:

```
config-service/src/main/resources/configurations/
```

### Configuración JWT

La configuración JWT está centralizada para todos los servicios en el archivo `application.properties` del Config Server. Esta configuración incluye:

- `authorization.jwt.secret`: Clave secreta compartida para firmar y verificar tokens JWT
- `authorization.jwt.expiration.days`: Duración de los tokens JWT (específico para cada servicio)

Esta configuración centralizada garantiza que:
1. Todos los servicios usen la misma clave secreta para validación de tokens
2. Se reduzca la duplicación de configuración
3. Se facilite la rotación de claves en el futuro
4. Se mejore la seguridad al tener un único punto de gestión de secretos

## Ejecución

Para ejecutar el proyecto:

1. Inicie el config-service
2. Inicie el eureka-service
3. Inicie los demás servicios (iam-service, post-service, etc.)

## Seguridad

Los endpoints están protegidos mediante Spring Security y JWT. Para acceder a endpoints protegidos:

1. Obtenga un token a través del endpoint de autenticación del iam-service
2. Incluya el token en el header de sus solicitudes:
   ```
   Authorization: Bearer <token>
   ``` 