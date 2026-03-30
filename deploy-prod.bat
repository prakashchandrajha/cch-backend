@echo off
SETLOCAL ENABLEDELAYEDEXPANSION

REM Set variables
set IMAGE_NAME=cch
set CONTAINER_NAME=cch
set DOCKER_IMAGE=maven:3.9.9-amazoncorretto-21

REM Switch Docker context to default
docker context use default

REM Step 3: Pull the required Maven Docker image
echo Pulling Docker image with Java 21 and Maven...
docker pull %DOCKER_IMAGE%

REM Step 4: Build the Maven project inside Docker
echo Building the project using Maven inside Docker...
docker run --rm -v "%cd%":/usr/src/mymaven -w /usr/src/mymaven %DOCKER_IMAGE% mvn clean install

REM GET Current Image ID
FOR /F "delims=" %%i IN ('docker inspect --format "{{.Image}}" %CONTAINER_NAME% 2^>nul') DO SET IMAGE_ID=%%i
echo IMAGE ID: %IMAGE_ID%

REM Switch Docker context to ica-server
docker context use ica-server

REM Step 5: Build the Docker image with unique and latest tags
FOR /F "delims=" %%i IN ('git rev-parse --short HEAD') DO SET IMAGE_TAG=%%i
echo Building Docker image with tags: %IMAGE_NAME%:%IMAGE_TAG% and %IMAGE_NAME%:latest...
docker build -t %IMAGE_NAME%:%IMAGE_TAG% -t %IMAGE_NAME%:latest .

REM Step 6: Stop and remove the existing container (if running)
echo Stopping and removing the existing container (if any)...
docker rm -f %CONTAINER_NAME% 2>nul

REM Step 7: Remove old unused Docker images
echo Cleaning up old Docker images...
IF DEFINED IMAGE_ID (
docker rmi %IMAGE_ID%
)

REM Step 8: Deploy the new container
echo Deploying the new Docker container...
docker run -d ^
--restart always ^
--network host ^
--name %CONTAINER_NAME% ^
%IMAGE_NAME%:latest

echo Deployment completed successfully!

ENDLOCAL
