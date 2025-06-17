# Script basico para iniciar microservicios en orden
# Uso: .\start-services-basic.ps1

Write-Host "=== INICIANDO MICROSERVICIOS ===" -ForegroundColor Green
Write-Host ""

# Funcion para iniciar servicio
function Start-Service {
    param(
        [string]$ServiceName,
        [string]$ServicePath,
        [int]$Port
    )
    
    Write-Host "Iniciando $ServiceName en puerto $Port..." -ForegroundColor Yellow
    
    # Cambiar al directorio del servicio
    Set-Location $ServicePath
    
    # Iniciar el servicio en segundo plano
    Start-Process -FilePath ".\mvnw.cmd" -ArgumentList "spring-boot:run" -WindowStyle Minimized
    
    Write-Host "$ServiceName iniciado!" -ForegroundColor Green
    Write-Host ""
}

# Volver al directorio raiz
Set-Location "C:\Users\ALEXANDER.CASTILLO\Desktop\UNIVERSIDAD\microservicios-aventurape"

# 1. CONFIG SERVICE (Puerto 8888)
Write-Host "=== 1. CONFIG SERVICE ===" -ForegroundColor Blue
Start-Service "Config Service" "config-service" 8888

# Esperar 10 segundos
Write-Host "Esperando 10 segundos..." -ForegroundColor Gray
Start-Sleep -Seconds 10

# 2. EUREKA SERVICE (Puerto 8761)
Write-Host "=== 2. EUREKA SERVICE ===" -ForegroundColor Blue
Start-Service "Eureka Service" "eureka-service" 8761

# Esperar 10 segundos
Write-Host "Esperando 10 segundos..." -ForegroundColor Gray
Start-Sleep -Seconds 10

# 3. GATEWAY SERVICE (Puerto 8080)
Write-Host "=== 3. GATEWAY SERVICE ===" -ForegroundColor Blue
Start-Service "Gateway Service" "gateway-service" 8080

# Esperar 10 segundos
Write-Host "Esperando 10 segundos..." -ForegroundColor Gray
Start-Sleep -Seconds 10

# 4. IAM SERVICE (Puerto 8081)
Write-Host "=== 4. IAM SERVICE ===" -ForegroundColor Blue
Start-Service "IAM Service" "iam-service" 8081

# Esperar 10 segundos
Write-Host "Esperando 10 segundos..." -ForegroundColor Gray
Start-Sleep -Seconds 10

# 5. POST SERVICE (Puerto 8082)
Write-Host "=== 5. POST SERVICE ===" -ForegroundColor Blue
Start-Service "Post Service" "post-service" 8082

Write-Host ""
Write-Host "=== TODOS LOS SERVICIOS INICIADOS ===" -ForegroundColor Green
Write-Host "Config: http://localhost:8888" -ForegroundColor Cyan
Write-Host "Eureka: http://localhost:8761" -ForegroundColor Cyan
Write-Host "Gateway: http://localhost:8080" -ForegroundColor Cyan
Write-Host "IAM: http://localhost:8081" -ForegroundColor Cyan
Write-Host "Post: http://localhost:8082" -ForegroundColor Cyan
Write-Host ""
Write-Host "Los servicios estan ejecutandose en segundo plano." -ForegroundColor Yellow 