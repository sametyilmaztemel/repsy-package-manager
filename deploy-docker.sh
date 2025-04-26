#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

# Configuration
DOCKER_USERNAME="sametyilmaztemel"
DOCKER_PASSWORD="Pasazade@1998"
DOCKER_IMAGE_NAME="package-manager"
DOCKER_TAG="latest"

# Function to print status messages
print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

# Function to handle errors
handle_error() {
    print_error "$1"
    exit 1
}

# Check if Docker daemon is running
if ! docker info > /dev/null 2>&1; then
    print_error "Docker daemon is not running. Please start Docker Desktop first."
    exit 1
fi

# Create Dockerfile if it doesn't exist
if [ ! -f "Dockerfile" ]; then
    print_status "Creating Dockerfile..."
    echo 'FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]' > Dockerfile
fi

# Build the main application if not already built
if [ ! -d "target" ] || [ -z "$(ls -A target/*.jar 2>/dev/null)" ]; then
    print_status "Building the main application..."
    ./mvnw clean package -DskipTests
fi

# Check if JAR file exists
JAR_FILE=$(ls target/*.jar 2>/dev/null | head -1)
if [ -z "$JAR_FILE" ]; then
    handle_error "JAR file not found after build"
fi

print_status "Found JAR file: $JAR_FILE"

# Build Docker image
print_status "Building Docker image..."
docker build --no-cache -t $DOCKER_USERNAME/$DOCKER_IMAGE_NAME:$DOCKER_TAG . || handle_error "Docker build failed"

# Login to Docker Hub
print_status "Logging in to Docker Hub..."
echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
if [ $? -ne 0 ]; then
    handle_error "Docker login failed. Please check your credentials."
fi

# Push Docker image
print_status "Pushing Docker image to Docker Hub..."
docker push $DOCKER_USERNAME/$DOCKER_IMAGE_NAME:$DOCKER_TAG || handle_error "Docker push failed"

print_status "Docker deployment completed successfully!"
print_status "Docker Image: $DOCKER_USERNAME/$DOCKER_IMAGE_NAME:$DOCKER_TAG"
print_status "Docker Hub: https://hub.docker.com/r/$DOCKER_USERNAME/$DOCKER_IMAGE_NAME"
