# ADR 0001 - Use Hexagonal Architecture

## Status
Accepted

## Context

The project requires clear separation between business logic and infrastructure
to allow maintainability, testability, and technology independence.

## Decision

Adopt Hexagonal Architecture (Ports and Adapters).

Core principles:

- Domain logic isolated in the domain layer
- Ports define contracts
- Adapters implement infrastructure concerns

Layers:

- domain
- application
- adapters
- configuration

## Consequences

### Positive

- High testability
- Infrastructure independence
- Clear separation of concerns

### Negative

- Slightly higher initial complexity
- Requires discipline in enforcing boundaries