# RESTful API Service

This project provides a RESTful API service for managing resources. Below you will find details on how to access the Swagger UI for API documentation and the H2 Console for database management.

## Accessing Swagger UI
The Swagger UI is available for testing and exploring the API endpoints.

- **URL:** [http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)

## Accessing H2 Console
The H2 Console allows you to interact with the in-memory database used by the application.

- **URL:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- **JDBC URL:** `jdbc:h2:file:./h2test`
- **Username:** `sa`
- **Password:** Leave this field empty

## Setup Instructions
1. Clone the repository.
2. Build the project using Gradle or Maven:
   ```bash
   ./gradlew build
   # or
   mvn clean install
