# 🚀 Despliegue de Microservicios AventuraPE

## � Repositorios de Docker Hub

**Mi perfil:** https://hub.docker.com/u/aksoonie

**Imágenes disponibles:**
- ✅ `aksoonie/config-service:latest`
- ✅ `aksoonie/eureka-service:latest`
- ✅ `aksoonie/gateway-service:latest`
- ✅ `aksoonie/iam-service:latest`
- ✅ `aksoonie/profile-service:latest`
- ✅ `aksoonie/post-service:latest`
- ✅ `aksoonie/comments-service:latest`
- ✅ `aksoonie/subscriptions-service:latest`
- ✅ `aksoonie/favorites-service:latest`
- ✅ `aksoonie/stats-service:latest`

## �📋 Requisitos previos

1. **Docker Desktop** instalado y ejecutándose
2. **PostgreSQL** instalado y ejecutándose en puerto 5432
3. **Bases de datos** creadas:
   ```sql
   CREATE DATABASE iam;
   CREATE DATABASE post;
   CREATE DATABASE profile;
   CREATE DATABASE stats;
   CREATE DATABASE favorites;
   CREATE DATABASE subscription;
   CREATE DATABASE comments;
   ```

## ⚙️ Variables de entorno utilizadas

**Configuración general:**
- `DB_URL`: URL de conexión a PostgreSQL
- `DB_USER`: Usuario de PostgreSQL (postgres)
- `DB_PASS`: Contraseña de PostgreSQL (73105334)
- `EUREKA_URL`: URL del servidor Eureka
- `CONFIG_SERVER_URL`: URL del servidor de configuración

**Variables específicas:**
- `RABBITMQ_HOST`: Host de RabbitMQ
- `RABBITMQ_PORT`: Puerto de RabbitMQ
- `RABBITMQ_USERNAME`: Usuario de RabbitMQ
- `RABBITMQ_PASSWORD`: Contraseña de RabbitMQ
- `JWT_ISSUER_URI`: URI del emisor JWT
- `JWT_JWK_SET_URI`: URI del conjunto de claves JWT
- `COMMENTS_SERVICE_URL`: URL del servicio de comentarios
- `POST_SERVICE_URL`: URL del servicio de posts
- `SUBSCRIPTIONS_SERVICE_URL`: URL del servicio de suscripciones
- `ALLOWED_ORIGINS`: Orígenes permitidos para CORS

## 📌 Orden de inicio de servicios

1. **RabbitMQ** - Mensajería
2. **Config Service** - Configuración centralizada
3. **Eureka Service** - Service Discovery
4. **IAM Service** - Autenticación
5. **Profile Service** - Gestión de perfiles
6. **Post Service** - Gestión de posts
7. **Stats Service** - Estadísticas
8. **Favorites Service** - Favoritos
9. **Subscriptions Service** - Suscripciones
10. **Comments Service** - Comentarios
11. **Gateway Service** - API Gateway

## 🔧 Configuración de PostgreSQL

Asegúrate de que PostgreSQL esté configurado con:
- **Usuario:** postgres
- **Contraseña:** 73105334
- **Puerto:** 5432
- **Host:** localhost

## 📥 Pasos para el despliegue

### 1. Descargar el archivo de configuración
Descarga el archivo `docker-compose-deployment.yml`

### 2. Abrir terminal en la carpeta del archivo
```bash
cd ruta/donde/descargaste/el/archivo
```

### 3. Ejecutar los microservicios
```bash
docker-compose -f docker-compose-deployment.yml up -d
```

### 4. Verificar que todo esté corriendo
```bash
docker-compose -f docker-compose-deployment.yml ps
```

## 🌐 URLs de acceso

Una vez que todo esté ejecutándose:

- **Gateway (API Principal):** http://localhost:8080
- **Eureka Server:** http://localhost:8761
- **RabbitMQ Management:** http://localhost:15672 (admin/admin)
- **Config Server:** http://localhost:8888
- **IAM Service:** http://localhost:8081
- **Profile Service:** http://localhost:8082
- **Post Service:** http://localhost:8083
- **Comments Service:** http://localhost:8084
- **Favorites Service:** http://localhost:8085
- **Stats Service:** http://localhost:8086
- **Subscriptions Service:** http://localhost:8087

## 📊 Comandos útiles

```bash
# Ver logs de todos los servicios
docker-compose -f docker-compose-deployment.yml logs -f

# Ver logs de un servicio específico
docker-compose -f docker-compose-deployment.yml logs -f [nombre-servicio]

# Reiniciar un servicio
docker-compose -f docker-compose-deployment.yml restart [nombre-servicio]

# Detener todos los servicios
docker-compose -f docker-compose-deployment.yml down

# Detener y eliminar volúmenes
docker-compose -f docker-compose-deployment.yml down -v
```

## 🛠️ Solución de problemas

### Si un servicio no inicia:
1. Verifica que PostgreSQL esté ejecutándose
2. Verifica que las bases de datos existan
3. Revisa los logs: `docker-compose -f docker-compose-deployment.yml logs [servicio]`

### Si hay errores de conexión:
1. Espera 2-3 minutos para que todos los servicios se inicialicen
2. Verifica que Eureka esté corriendo: http://localhost:8761
