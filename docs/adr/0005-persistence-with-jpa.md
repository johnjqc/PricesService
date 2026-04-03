# ADR 0005 - Persistence using JPA

## Status
Accepted

## Context

The application requires relational database persistence
for pricing data.

## Decision

Use JPA with Hibernate as the ORM implementation.

Persistence adapters will:

- map entities
- implement repository interfaces
- isolate domain from database concerns

## Consequences

### Positive

- Reduced boilerplate
- Mature ORM ecosystem

### Negative

- ORM complexity