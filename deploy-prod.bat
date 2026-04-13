@echo off
SETLOCAL ENABLEDELAYEDEXPANSION

echo ==============================
echo 🚀 Starting Deployment
echo ==============================

REM -------------------------------
REM Config
REM -------------------------------
set IMAGE_NAME=cch
set CONTAINER_NAME=cch
set DOCKER_IMAGE=maven:3.9.9-amazoncorretto-21

REM -------------------------------
REM Step 1: Switch Docker context to default
REM -------------------------------
echo [Step 1] Switching Docker context to default...
docker context use default || goto :error

REM -------------------------------
REM Step 2: Pull Docker image
REM -------------------------------
echo [Step 2] Pulling Docker image...
docker pull %DOCKER_IMAGE% || goto :error

REM -------------------------------
REM Step 3: Build Maven project
REM -------------------------------
echo [Step 3] Building project with Maven...
docker run --rm -v "%cd%":/usr/src/mymaven -w /usr/src/mymaven %DOCKER_IMAGE% mvn clean install || goto :error

REM -------------------------------
REM Step 4: Get current container image ID (if exists)
REM -------------------------------
echo [Step 4] Fetching existing container image ID...
set IMAGE_ID=
FOR /F "delims=" %%i IN ('docker inspect --format "{{.Image}}" %CONTAINER_NAME% 2^>nul') DO SET IMAGE_ID=%%i
echo Current IMAGE ID: !IMAGE_ID!

REM -------------------------------
REM Step 5: Switch Docker context to server
REM -------------------------------
echo [Step 5] Switching Docker context to ica-server...
docker context use ica-server || goto :error

REM -------------------------------
REM Step 6: Get Git commit tag
REM -------------------------------
echo [Step 6] Getting Git commit tag...
FOR /F "delims=" %%i IN ('git rev-parse --short HEAD') DO SET IMAGE_TAG=%%i
IF "!IMAGE_TAG!"=="" (
    echo ❌ Failed to get Git tag
    goto :error
)
echo IMAGE TAG: !IMAGE_TAG!

REM -------------------------------
REM Step 7: Build Docker image
REM -------------------------------
echo [Step 7] Building Docker image...
docker build -t %IMAGE_NAME%:!IMAGE_TAG! -t %IMAGE_NAME%:latest . || goto :error

REM -------------------------------
REM Step 8: Remove old container (if exists)
REM -------------------------------
echo [Step 8] Removing old container (if exists)...
docker rm -f %CONTAINER_NAME% >nul 2>&1

REM -------------------------------
REM Step 9: Remove old image (if exists)
REM -------------------------------
echo [Step 9] Removing old image (if exists)...
IF DEFINED IMAGE_ID (
    docker rmi !IMAGE_ID! >nul 2>&1
)

REM -------------------------------
REM Step 10: Run new container
REM -------------------------------
echo [Step 10] Starting new container...
docker run -d ^
--restart always ^
--network host ^
--name %CONTAINER_NAME% ^
%IMAGE_NAME%:latest || goto :error

REM -------------------------------
REM SUCCESS
REM -------------------------------
echo ==============================
echo ✅ Deployment Successful!
echo ==============================
goto :end

REM -------------------------------
REM ERROR HANDLER
REM -------------------------------
:error
echo ==============================
echo ❌ Deployment Failed!
echo Check logs above.
echo ==============================
exit /b 1

:end
ENDLOCAL