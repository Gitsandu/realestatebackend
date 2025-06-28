# Real Estate Backend

A robust Spring Boot backend for a Real Estate Management System with JWT authentication, built with Java 17 and MongoDB.

## Features

- 🔐 **JWT Authentication** - Secure user authentication with JSON Web Tokens
- 🛡️ **Role-based Authorization** - Different access levels for Users and Admins
- 📝 **OpenAPI Documentation** - Interactive API documentation with Swagger UI
- 🚀 **Modern Stack** - Built with Spring Boot 3.x, Java 17, and MongoDB
- 🏗️ **Clean Architecture** - Well-structured n-layered architecture

## Tech Stack

- **Backend Framework**: Spring Boot 3.x
- **Language**: Java 17
- **Database**: MongoDB
- **Authentication**: JWT (JSON Web Tokens)
- **API Documentation**: SpringDoc OpenAPI 3.0
- **Build Tool**: Maven

## Prerequisites

- Java 17 or higher
- Maven 3.6.3 or higher
- MongoDB 4.4 or higher
- Git (for version control)

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/real-estate-backend.git
cd real-estate-backend
```

### 2. Configure the Application

Create an `application.yml` file in `src/main/resources/` with the following content:

```yaml
server:
  port: 8080
  servlet:
    context-path: /api

spring:
  data:
    mongodb:
      host: localhost
      port: 27017
      database: real_estate_db

jwt:
  secret: your-256-bit-secret-key-must-be-at-least-32-characters-long
  expiration: 86400000 # 24 hours in milliseconds

logging:
  level:
    org.springframework.web: INFO
    com.example.RealEstateBackend: DEBUG

springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html
    operationsSorter: method
```

### 3. Build and Run

```bash
# Build the application
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will be available at `http://localhost:8080/api`

## API Documentation

Once the application is running, you can access the following:

- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api/api-docs

## Authentication

The API uses JWT for authentication. Here's how to use it:

### 1. Register a new user

```http
POST /api/v1/auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "securePassword123",
  "role": "USER"
}
```

### 2. Login to get JWT token

```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "securePassword123"
}
```

### 3. Use the token in subsequent requests

```
Authorization: Bearer <your-jwt-token>
```

## Project Structure

```
src/main/java/com/example/RealEstateBackend/
├── config/               # Configuration classes
├── controller/           # REST controllers
├── dto/                  # Data Transfer Objects
│   └── auth/             # Authentication DTOs
├── model/                # Entity classes
├── repository/           # Data access layer
├── security/             # Security configurations and filters
├── service/              # Business logic
└── RealEstateBackendApplication.java  # Main application class
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Spring Boot Team
- MongoDB
- JWT for Java
- OpenAPI/Swagger