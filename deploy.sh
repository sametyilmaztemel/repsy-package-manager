#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

# Configuration
REPO_NAME="repsy-package-manager"
GITHUB_USERNAME="yourusername"
DOCKER_USERNAME="repsy"
DOCKER_IMAGE_NAME="package-manager"
EMAIL="contact@repsy.io"

# Function to print status messages
print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to check if a command exists
check_command() {
    if ! command -v $1 &> /dev/null; then
        print_error "$1 is not installed. Please install it first."
        exit 1
    fi
}

# Check required commands
print_status "Checking required commands..."
check_command git
check_command mvn
check_command docker
check_command curl

# Initialize Git repository if not already initialized
if [ ! -d ".git" ]; then
    print_status "Initializing Git repository..."
    git init
    git add .
    git commit -m "Initial commit: Repsy Package Manager implementation"
    git branch -M main
fi

# Create GitHub repository
print_status "Creating GitHub repository..."
curl -u $GITHUB_USERNAME https://api.github.com/user/repos -d "{\"name\":\"$REPO_NAME\",\"public\":true}"
git remote add origin https://github.com/$GITHUB_USERNAME/$REPO_NAME.git
git push -u origin main

# Create storage modules
print_status "Creating storage modules..."
mkdir -p ../repsy-storage-file-system/src/main/java/com/repsy/storage/filesystem
mkdir -p ../repsy-storage-object-storage/src/main/java/com/repsy/storage/objectstorage

# Copy storage implementations
cp -r src/main/java/com/repsy/packagemanager/storage/filesystem/* ../repsy-storage-file-system/src/main/java/com/repsy/storage/filesystem/
cp -r src/main/java/com/repsy/packagemanager/storage/objectstorage/* ../repsy-storage-object-storage/src/main/java/com/repsy/storage/objectstorage/

# Create pom.xml files for storage modules
print_status "Creating pom.xml files for storage modules..."
cat > ../repsy-storage-file-system/pom.xml << EOL
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.repsy</groupId>
    <artifactId>repsy-storage-file-system</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
</project>
EOL

cat > ../repsy-storage-object-storage/pom.xml << EOL
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.repsy</groupId>
    <artifactId>repsy-storage-object-storage</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
</project>
EOL

# Create Dockerfile
print_status "Creating Dockerfile..."
cat > Dockerfile << EOL
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
EOL

# Build the project
print_status "Building the project..."
./mvnw clean install

# Build Docker image
print_status "Building Docker image..."
docker build -t $DOCKER_USERNAME/$DOCKER_IMAGE_NAME:latest .

# Login to Docker Hub
print_status "Logging in to Docker Hub..."
docker login

# Push Docker image
print_status "Pushing Docker image..."
docker push $DOCKER_USERNAME/$DOCKER_IMAGE_NAME:latest

# Send email
print_status "Sending submission email..."
cat > submission_email.txt << EOL
Subject: Repsy Package Manager Implementation Submission

Merhaba,

Repsy Package Manager implementasyonunu tamamladım. Detaylar aşağıdadır:

1. GitHub Repository:
   - URL: https://github.com/$GITHUB_USERNAME/$REPO_NAME
   - Commit geçmişi düzgün şekilde tutulmuştur

2. Storage Modülleri:
   - File System Storage: https://github.com/$GITHUB_USERNAME/repsy-storage-file-system
   - Object Storage: https://github.com/$GITHUB_USERNAME/repsy-storage-object-storage

3. Docker Container:
   - Image: $DOCKER_USERNAME/$DOCKER_IMAGE_NAME:latest
   - Docker Hub: https://hub.docker.com/r/$DOCKER_USERNAME/$DOCKER_IMAGE_NAME

4. Proje Dokümantasyonu:
   - README.md ve README.tr.md dosyaları hazırlanmıştır
   - Kurulum adımları ve API kullanımı detaylı olarak belgelenmiştir

Projeyi incelemenizi rica ederim. Herhangi bir sorunuz olursa yardımcı olmaktan memnuniyet duyarım.

Saygılarımla,
$(git config user.name)
EOL

# Send email using curl
curl --url "smtps://smtp.gmail.com:465" --ssl-reqd \
  --mail-from "$(git config user.email)" \
  --mail-rcpt "$EMAIL" \
  --upload-file submission_email.txt \
  --user "$(git config user.email):your-app-password"

print_status "Deployment completed successfully!" 