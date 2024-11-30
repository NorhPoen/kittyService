# Cat Management Service

## Description
This project is a part of a series of lab exercises aimed at creating a comprehensive service for managing cats and their owners. The final stage involves breaking the application into microservices, each with a specific role, and securing the services with proper authorization mechanisms.

## Microservices
The application consists of three microservices:
1. **Cat Service**: Handles operations related to cats.
2. **Owner Service**: Handles operations related to cat owners.
3. **Gateway Service**: Provides external interfaces and handles communication between microservices.

### Technologies Used
- **Spring Boot**: To create standalone, production-grade Spring based Applications.
- **Spring Data JPA**: For data access and management.
- **Spring Security**: To secure endpoints with roles and permissions.
- **RabbitMQ/Kafka**: For communication between microservices.
- **PostgreSQL**: Database management.
- **Hibernate**: ORM tool to manage database interactions.
- **Maven/Gradle**: Build automation tools.
- **JUnit**: Testing framework.
- **Mockito**: For mocking database connections during testing.

## Setup and Build Instructions
1. **Clone the repository**:
   ```bash
   git clone <repository_url>
