@echo off
set MICROSERVICE=%1
set VERSION=%2

if "%MICROSERVICE%"=="" (
    echo Uso: build-microservice.bat [nombre-microservicio] [version]
    echo Ejemplo: build-microservice.bat usuarios-tokens v1.0
    exit /b 1
)

if "%VERSION%"=="" set VERSION=v1.0

echo Construyendo microservicio: %MICROSERVICE%
docker build -f microservices/%MICROSERVICE%/Dockerfile -t roberto523/%MICROSERVICE%-service:%VERSION% microservices/%MICROSERVICE%/

echo Subiendo a Docker Hub...
docker push roberto523/%MICROSERVICE%-service:%VERSION%

echo ¡Listo!
