## 1.0.0 (2026-04-03)

### Added
- Hexagonal Architecture implementation with domain, application, and adapters modules
- REST API endpoint (`GET /api/price`) for retrieving product prices by brand, product ID, and application date
- OpenAPI/Swagger documentation with detailed API descriptions
- Input validation using Bean Validation (`@NotNull` constraints)
- Unit tests for use case, repository, and controller layers
- End-to-end tests with H2 in-memory database covering all price scenarios
- Domain exceptions (`PriceNotFoundException`, `DomainException`) with centralized error handling
- Error code enum for standardized error responses
- Domain login on entity domain
