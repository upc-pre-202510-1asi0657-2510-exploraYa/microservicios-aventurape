@echo off
set DOCKER_USER=aksoonie

echo Taggeando imagenes...
REM Config Service
docker tag config-service %DOCKER_USER%/config-service:latest

REM Eureka Service
docker tag eureka-service %DOCKER_USER%/eureka-service:latest

REM Gateway Service
docker tag gateway-service %DOCKER_USER%/gateway-service:latest

REM IAM Service
docker tag iam-service %DOCKER_USER%/iam-service:latest

REM Profile Service
docker tag profile-service %DOCKER_USER%/profile-service:latest

REM Post Service
docker tag post-service %DOCKER_USER%/post-service:latest

REM Comments Service
docker tag comments-service %DOCKER_USER%/comments-service:latest

REM Subscriptions Service
docker tag subscriptions-service %DOCKER_USER%/subscriptions-service:latest

REM Favorites Service
docker tag favorites-service %DOCKER_USER%/favorites-service:latest

REM Stats Service
docker tag stats-service %DOCKER_USER%/stats-service:latest

echo Publicando en Docker Hub...
docker push %DOCKER_USER%/config-service:latest
docker push %DOCKER_USER%/eureka-service:latest
docker push %DOCKER_USER%/gateway-service:latest
docker push %DOCKER_USER%/iam-service:latest
docker push %DOCKER_USER%/profile-service:latest
docker push %DOCKER_USER%/post-service:latest
docker push %DOCKER_USER%/comments-service:latest
docker push %DOCKER_USER%/subscriptions-service:latest
docker push %DOCKER_USER%/favorites-service:latest
docker push %DOCKER_USER%/stats-service:latest

echo Todas las imagenes han sido publicadas correctamente!
pause