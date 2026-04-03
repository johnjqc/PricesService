# PricesService

A backend service for retrieving product prices based on brand, product, and application date. The service implements **Hexagonal Architecture (Ports and Adapters)** to ensure clean separation of concerns and testability.

---

## Architecture

This project follows **Hexagonal Architecture** (also known as Ports and Adapters), which establishes a clear boundary between the core business logic and external systems.

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              BOOTSTRAP                                      │
│                    (Application Entry Point)                               │
└────────────────────────────────┬────────────────────────────────────────────┘
                                 │
         ┌───────────────────────┼───────────────────────┐
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────────┐
│      DOMAIN     │    │   APPLICATION  │    │       ADAPTERS      │
│                 │    │                 │    │                     │
│  - Models       │    │  - Use Cases    │    │  REST Adapter       │
│  - Exceptions   │    │  - Input Ports  │    │  (PriceController) │
│  - Business     │    │  - Output Ports │    │                     │
│    Logic        │    │                 │    │  Persistence Adapter│
│                 │    │                 │    │  (JPA Repository)   │
└─────────────────┘    └─────────────────┘    └─────────────────────┘
```

### Layer Responsibilities

| Layer | Description |
|-------|-------------|
| **Domain** | Core business entities, exceptions, and domain logic. Has no external dependencies. |
| **Application** | Use cases, input/output ports, and DTOs. Orchestrates the flow between domain and adapters. |
| **Adapters** | Implementations of ports. REST adapter exposes endpoints; Persistence adapter handles database operations. |
| **Bootstrap** | Spring Boot application entry point that wires all components together. |

---

## Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 21 | Programming language |
| Spring Boot | 3.2.5 | Application framework |
| Gradle | 8.x | Build tool |
| JUnit 5 | 5.10.2 | Unit testing |
| H2 Database | - | In-memory database for testing |
| SpringDoc OpenAPI | 2.5.0 | API documentation (Swagger) |
| AssertJ | 3.25.3 | Testing assertions |
| Mockito | 5.8.0 | Mocking framework |

---

## Project Structure

```
PricesService/
├── domain/                          # Core business logic (no dependencies)
│   └── src/main/java/.../
│       ├── model/Price.java         # Domain entity
│       └── exception/                # Domain exceptions
│
├── application/                     # Use cases and ports
│   └── src/main/java/.../
│       ├── port/
│       │   ├── in/GetProductPrice.java      # Input port (interface)
│       │   └── outp/PriceRepositoryPort.java # Output port (interface)
│       ├── usecase/                          # Use case implementations
│       └── dto/                              # Application DTOs
│
├── adapters/
│   ├── rest-adapter/                # REST API layer
│   │   └── src/main/java/.../
│   │       ├── rest/                       # Controllers
│   │       │   └── PriceController.java
│   │       ├── factory/                     # Response factories
│   │       ├── dto/                         # API DTOs
│   │       └── advice/                      # Exception handlers
│   │
│   └── persistence-adapter/          # Database layer
│       └── src/main/java/.../
│           ├── entity/                     # JPA entities
│           ├── repository/                   # JPA repositories
│           ├── mapper/                       # Entity-Domain mappers
│           └── adapter/                      # Port implementations
│
└── bootstrap/                        # Application entry point
    └── src/main/java/.../
        ├── ApplicationBootstrap.java         # Spring Boot main class
        └── config/                           # Bean configuration
```

---

## API

### Main Endpoint

`GET /api/price`

Retrieves the applicable price for a product based on brand and application date.

#### Request Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `productId` | Long | Yes | Product identifier |
| `brandId` | Long | Yes | Brand identifier |
| `applicationDate` | LocalDateTime | Yes | Date and time in ISO 8601 format |

#### Example Request

```bash
curl -X GET "http://localhost:8080/api/price?productId=35455&brandId=1&applicationDate=2020-06-14T10:00:00"
```

#### Response

```json
{
  "data": {
    "productId": 35455,
    "brandId": 1,
    "priceList": 1,
    "startDate": "2020-06-14T00:00:00",
    "endDate": "2020-12-31T23:59:59",
    "price": 35.50,
    "currency": "EUR"
  },
  "notification": {
    "description": "Operation successful",
    "responseTime": "2026-04-03T10:30:00",
    "code": "SCS-00"
  }
}
```

#### Error Responses

| Code | Description |
|------|-------------|
| 400 | Bad Request - Invalid parameters |
| 404 | Price not found for the given criteria |

---

## Getting Started

### Requirements

To run the application locally you need:

- **Java**: 21 or higher
- **Gradle**: 8.x (wrapper included)

Alternatively, you can run the application using **Docker**, without installing Java or Gradle.

### Build the Project

```bash
./gradlew build
```

### Run the Application

```bash
./gradlew :bootstrap:bootRun
```

### Running with Docker

The application can also be executed using Docker without installing Java or Gradle locally.

#### Build the Docker Image

```bash
docker build -t prices-service .
docker run -p 8080:8080 prices-service
```

The application will start on `http://localhost:8080`.

### Run Tests

```bash
# Run all tests
./gradlew test

# Run tests with detailed output
./gradlew test --info
```

### API Documentation

When the application is running, access the Swagger UI at:

```
http://localhost:8080/swagger-ui.html
```

---

## Testing

The project includes multiple test layers:

| Test Type | Location | Description |
|-----------|----------|-------------|
| **Unit Tests** | `domain/`, `application/` | Test domain logic and use cases |
| **Repository Tests** | `persistence-adapter/` | Test JPA queries with H2 |
| **Controller Tests** | `rest-adapter/` | Test REST endpoints with mocked services |
| **E2E Tests** | `bootstrap/` | Full integration tests with real database |

### Test Coverage

- Domain model logic (price resolution by priority)
- Use case orchestration
- REST API endpoints
- JPA repository queries
- Exception handling

---

## Changelog

All notable changes to this project are documented in the [CHANGELOG.md](CHANGELOG.md) file.

---

## License

This project is for demonstration purposes.