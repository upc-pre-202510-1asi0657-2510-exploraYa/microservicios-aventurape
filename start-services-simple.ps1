# Script simple para iniciar microservicios en orden
# Ejecutar en la terminal de IntelliJ IDEA

Write-Host "=== INICIANDO MICROSERVICIOS EN ORDEN ===" -ForegroundColor Green
Write-Host ""

# Función para iniciar servicio y esperar
function Start-And-Wait {
    param(
        [string]$ServiceName,
        [string]$ServicePath,
        [string]$HealthUrl,
        [int]$Port
    )
    
    Write-Host "Iniciando $ServiceName en puerto $Port..." -ForegroundColor Yellow
    
    # Cambiar al directorio del servicio
    Set-Location $ServicePath
    
    # Iniciar el servicio
    Start-Process -FilePath ".\mvnw.cmd" -ArgumentList "spring-boot:run" -WindowStyle Minimized
    
    # Esperar a que el servicio esté listo
    Write-Host "Esperando a que $ServiceName esté listo..." -ForegroundColor Cyan
    
    $attempts = 0
    $maxAttempts = 30
    
    while ($attempts -lt $maxAttempts) {
        try {
            $response = Invoke-WebRequest -Uri $HealthUrl -Method GET -TimeoutSec 3 -UseBasicParsing
            if ($response.StatusCode -eq 200) {
                Write-Host "$ServiceName está listo! ✓" -ForegroundColor Green
                break
            }
        }
        catch {
            $attempts++
            Write-Host "Intento $attempts/$maxAttempts - Esperando..." -ForegroundColor Gray
            Start-Sleep -Seconds 3
        }
    }
    
    if ($attempts -eq $maxAttempts) {
        Write-Host "ADVERTENCIA: $ServiceName puede no estar completamente listo" -ForegroundColor Yellow
    }
    
    Write-Host ""
}

# Volver al directorio raíz
Set-Location "C:\Users\ALEXANDER.CASTILLO\Desktop\UNIVERSIDAD\microservicios-aventurape"

# 1. CONFIG SERVICE (Puerto 8888)
Start-And-Wait "Config Service" "config-service" "http://localhost:8888/actuator/health" 8888

# 2. EUREKA SERVICE (Puerto 8761)
Start-And-Wait "Eureka Service" "eureka-service" "http://localhost:8761/actuator/health" 8761

# 3. GATEWAY SERVICE (Puerto 8080)
Start-And-Wait "Gateway Service" "gateway-service" "http://localhost:8080/actuator/health" 8080

# 4. IAM SERVICE (Puerto 8081)
Start-And-Wait "IAM Service" "iam-service" "http://localhost:8081/actuator/health" 8081

# 5. POST SERVICE (Puerto 8082)
Start-And-Wait "Post Service" "post-service" "http://localhost:8082/actuator/health" 8082

Write-Host "=== TODOS LOS SERVICIOS INICIADOS ===" -ForegroundColor Green
Write-Host "Config: http://localhost:8888" -ForegroundColor Cyan
Write-Host "Eureka: http://localhost:8761" -ForegroundColor Cyan
Write-Host "Gateway: http://localhost:8080" -ForegroundColor Cyan
Write-Host "IAM: http://localhost:8081" -ForegroundColor Cyan
Write-Host "Post: http://localhost:8082" -ForegroundColor Cyan
Write-Host ""
Write-Host "Los servicios estan ejecutandose en segundo plano." -ForegroundColor Yellow 