# Script para iniciar los microservicios en orden específico
# Uso: .\start-services.ps1

Write-Host "=== INICIANDO MICROSERVICIOS AVENTURAPE ===" -ForegroundColor Green
Write-Host "Orden de inicio: config -> eureka -> gateway -> iam -> post" -ForegroundColor Yellow
Write-Host ""

# Función para esperar a que un servicio esté listo
function Wait-ForService {
    param(
        [string]$ServiceName,
        [string]$Url,
        [int]$MaxAttempts = 30,
        [int]$DelaySeconds = 5
    )
    
    Write-Host "Esperando a que $ServiceName esté listo..." -ForegroundColor Cyan
    
    for ($i = 1; $i -le $MaxAttempts; $i++) {
        try {
            $response = Invoke-WebRequest -Uri $Url -Method GET -TimeoutSec 5 -UseBasicParsing
            if ($response.StatusCode -eq 200) {
                Write-Host "$ServiceName está listo! (Intento $i/$MaxAttempts)" -ForegroundColor Green
                return $true
            }
        }
        catch {
            Write-Host "Intento $i/$MaxAttempts - $ServiceName aún no está listo..." -ForegroundColor Yellow
        }
        
        if ($i -lt $MaxAttempts) {
            Start-Sleep -Seconds $DelaySeconds
        }
    }
    
    Write-Host "ERROR: $ServiceName no respondió después de $MaxAttempts intentos" -ForegroundColor Red
    return $false
}

# Función para iniciar un servicio
function Start-Service {
    param(
        [string]$ServiceName,
        [string]$ServicePath
    )
    
    Write-Host "Iniciando $ServiceName..." -ForegroundColor Magenta
    
    # Cambiar al directorio del servicio
    Set-Location $ServicePath
    
    # Iniciar el servicio en segundo plano
    $process = Start-Process -FilePath ".\mvnw.cmd" -ArgumentList "spring-boot:run" -PassThru -WindowStyle Minimized
    
    # Guardar el proceso para poder detenerlo después si es necesario
    $global:processes += $process
    
    Write-Host "$ServiceName iniciado con PID: $($process.Id)" -ForegroundColor Green
    return $process
}

# Inicializar array de procesos
$global:processes = @()

try {
    # 1. CONFIG SERVICE
    Write-Host "=== 1. INICIANDO CONFIG SERVICE ===" -ForegroundColor Blue
    $configProcess = Start-Service "Config Service" "config-service"
    Wait-ForService "Config Service" "http://localhost:8888/actuator/health"
    
    # 2. EUREKA SERVICE
    Write-Host "=== 2. INICIANDO EUREKA SERVICE ===" -ForegroundColor Blue
    $eurekaProcess = Start-Service "Eureka Service" "eureka-service"
    Wait-ForService "Eureka Service" "http://localhost:8761/actuator/health"
    
    # 3. GATEWAY SERVICE
    Write-Host "=== 3. INICIANDO GATEWAY SERVICE ===" -ForegroundColor Blue
    $gatewayProcess = Start-Service "Gateway Service" "gateway-service"
    Wait-ForService "Gateway Service" "http://localhost:8080/actuator/health"
    
    # 4. IAM SERVICE
    Write-Host "=== 4. INICIANDO IAM SERVICE ===" -ForegroundColor Blue
    $iamProcess = Start-Service "IAM Service" "iam-service"
    Wait-ForService "IAM Service" "http://localhost:8081/actuator/health"
    
    # 5. POST SERVICE
    Write-Host "=== 5. INICIANDO POST SERVICE ===" -ForegroundColor Blue
    $postProcess = Start-Service "Post Service" "post-service"
    Wait-ForService "Post Service" "http://localhost:8082/actuator/health"
    
    Write-Host ""
    Write-Host "=== TODOS LOS SERVICIOS INICIADOS EXITOSAMENTE ===" -ForegroundColor Green
    Write-Host "Config Service: http://localhost:8888" -ForegroundColor Cyan
    Write-Host "Eureka Service: http://localhost:8761" -ForegroundColor Cyan
    Write-Host "Gateway Service: http://localhost:8080" -ForegroundColor Cyan
    Write-Host "IAM Service: http://localhost:8081" -ForegroundColor Cyan
    Write-Host "Post Service: http://localhost:8082" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Presiona Ctrl+C para detener todos los servicios" -ForegroundColor Yellow
    
    # Mantener el script ejecutándose
    while ($true) {
        Start-Sleep -Seconds 10
    }
}
catch {
    Write-Host "Error durante la ejecución: $($_.Exception.Message)" -ForegroundColor Red
}
finally {
    # Detener todos los procesos al salir
    Write-Host ""
    Write-Host "Deteniendo todos los servicios..." -ForegroundColor Yellow
    
    foreach ($process in $global:processes) {
        if ($process -and -not $process.HasExited) {
            Write-Host "Deteniendo proceso PID: $($process.Id)" -ForegroundColor Yellow
            Stop-Process -Id $process.Id -Force
        }
    }
    
    Write-Host "Todos los servicios han sido detenidos." -ForegroundColor Green
} 