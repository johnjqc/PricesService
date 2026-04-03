# ADR 0002 - API First with OpenAPI

## Status
Accepted

## Context

The system exposes REST APIs that should be well documented and
contract-driven.

## Decision

Adopt an API-first approach using OpenAPI specification.

The OpenAPI contract defines:

- endpoints
- request/response models
- HTTP codes
- documentation

Implementation must comply with the specification.

## Consequences

### Positive

- Clear API contracts
- Better collaboration
- Easier client integration

### Negative

- Additional maintenance of specification