# Full Stack Augmented - Users CRUD API

## Overview
This project is a Java 21 Spring Boot REST API for managing users stored in an H2 in-memory database. It uses a layered architecture with controller, service, and repository/DAO layers, plus global exception handling and OpenAPI documentation.

## Architecture
- `controller` layer: exposes REST endpoints under `/api/users`
- `service` layer: business logic and validation
- `repository` layer: `JpaRepository` persistence for `User` entity
- `exception` layer: custom exceptions and centralized error responses
- `entity` layer: JPA entity mapping for the `users` table

## Project Structure
- `pom.xml`: Maven build file and dependencies
- `openapi.yaml`: OpenAPI 3 specification for CRUD endpoints
- `src/main/java/io/fullstack/augmented`: application code
- `src/main/resources/application-local.properties`: local runtime configuration
- `src/main/resources/schema.sql`: DDL schema initialization for H2
- `src/test/java/io/fullstack/augmented`: unit tests with Mockito

## API Endpoints
Base URL: `http://localhost:8080`

### Users
- `GET /api/users`
  - Returns all users
- `GET /api/users/{id}`
  - Returns user by ID
- `POST /api/users`
  - Creates a new user
  - Body: `{ "username": "name", "email": "email@example.com" }`
- `PUT /api/users/{id}`
  - Updates an existing user
  - Body: `{ "username": "new-name", "email": "new-email@example.com" }`
- `DELETE /api/users/{id}`
  - Deletes a user by ID

## Configuration
The application uses the `local` profile by default. Local configuration is stored in `src/main/resources/application-local.properties`.

Key settings:
- H2 in-memory database: `jdbc:h2:mem:usersdb`
- H2 console: `/h2-console`
- OpenAPI docs: `/api-docs`
- Swagger UI: `/swagger-ui.html`

## Running the project
Build the project:
```bash
mvn clean package
```
Run the application:
```bash
mvn spring-boot:run
```
Or start the generated JAR:
```bash
java -jar target/augmented-api-0.0.1-SNAPSHOT.jar
```

## Testing
Run unit tests:
```bash
mvn test
```

## Notes
- Schema initialization is handled by `schema.sql`.
- Log messages are emitted for key events like create/update/delete operations.
- Exceptions are translated to meaningful HTTP responses by `GlobalExceptionHandler`.
