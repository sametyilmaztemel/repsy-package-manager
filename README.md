# Repsy Package Manager

A Spring Boot application for managing and distributing software packages.

## Features

- Package deployment with versioning
- Package download functionality
- Secure authentication and authorization
- File size limit enforcement
- RESTful API endpoints
- Swagger documentation

## Prerequisites

- Java 17 or higher
- Maven 3.8 or higher
- Git

## Setup and Installation

1. Clone the repository:
```bash
git clone https://github.com/yourusername/repsy-package-manager.git
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
src/
├── main/
│   ├── java/
│   │   └── com/repsy/packagemanager/
│   │       ├── config/         # Configuration classes
│   │       ├── controller/     # REST controllers
│   │       ├── entity/         # JPA entities
│   │       ├── exception/      # Exception handling
│   │       ├── model/          # Data models
│   │       ├── repository/     # JPA repositories
│   │       ├── service/        # Business logic
│   │       └── storage/        # File storage strategies
│   └── resources/
│       └── application.yml     # Application configuration
└── test/                       # Test classes
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
