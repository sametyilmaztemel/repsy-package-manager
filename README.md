# Repsy Package Manager

A Spring Boot application for managing and distributing software packages.

## Features

- Package deployment with versioning
- Package download functionality
- Secure authentication and authorization
- File size limit enforcement
- RESTful API endpoints
- Swagger documentation
- Modular storage architecture (File System and Object Storage)

## Prerequisites

- Java 17 or higher
- Maven 3.8 or higher
- Git

## Setup and Installation

1. Clone the repository:
```bash
git clone https://github.com/sametyilmaztemel/repsy-package-manager.git
cd repsy-package-manager
```

2. Build the project:
```bash
./mvnw clean install
```

3. Run the application:
```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

## Storage Modules

Repsy Package Manager uses a modular storage architecture with two implementations:

### File System Storage

A storage implementation that saves packages to the local file system.

- Repository: [repsy-storage-file-system](https://github.com/sametyilmaztemel/repsy-storage-file-system)
- Features:
  - Local file system based storage
  - Directory structure by package/version
  - Path validation and security

### Object Storage

A storage implementation that saves packages to object storage services like MinIO or S3.

- Repository: [repsy-storage-object-storage](https://github.com/sametyilmaztemel/repsy-storage-object-storage)
- Features:
  - Compatible with S3-like object storage (MinIO, AWS S3, etc.)
  - Bucket-based organization
  - Streaming upload/download

### Deploying Storage Modules

The project includes a deployment script `deploy-github.sh` that facilitates deploying the storage modules as separate GitHub repositories:

```bash
# Make the script executable
chmod +x deploy-github.sh

# Run the deployment script
./deploy-github.sh
```

The script offers two options:
1. Direct deployment to GitHub using personal access token
2. Creating ZIP archives for manual upload

## API Documentation

Once the application is running, you can access the Swagger documentation at:
`http://localhost:8080/swagger-ui.html`

## Security

The application uses Spring Security with basic authentication. By default:
- Package download endpoints are publicly accessible
- Package deployment endpoints require authentication
- CSRF protection is disabled for simplicity

## Project Structure

```
./
├── src/                        # Main application code
├── storage-modules/            # Storage module implementations
│   ├── repsy-storage-file-system/
│   └── repsy-storage-object-storage/
├── deploy.sh                   # Main deployment script
├── deploy-github.sh            # Storage modules deployment script
└── deploy-docker.sh            # Docker deployment script
```

## Testing

Run the tests using:
```bash
./mvnw test
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## Deployment

The project includes an automated deployment script that handles the entire deployment process. To use it:

1. Configure the script:
   ```bash
   # Edit deploy.sh and update these variables:
   GITHUB_USERNAME="yourusername"
   DOCKER_USERNAME="repsy"
   DOCKER_IMAGE_NAME="package-manager"
   EMAIL="contact@repsy.io"
   ```

2. Make the script executable:
   ```bash
   chmod +x deploy.sh
   ```

3. Run the deployment script:
   ```bash
   ./deploy.sh
   ```

The script will:
- Initialize Git repository if not already done
- Create a public GitHub repository
- Separate storage modules into distinct libraries
- Create Dockerfile and build Docker image
- Push the Docker image to Docker Hub
- Send submission email with all necessary links
