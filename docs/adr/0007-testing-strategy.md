# ADR 0007 - Testing Strategy

## Status
Accepted

## Context

The system must maintain high quality and reliability.

## Decision

Adopt a multi-layer testing strategy:

Unit Tests
- domain logic
- services

Integration Tests
- persistence adapters
- REST controllers

Tools:

- JUnit
- Mockito
- Spring Test

## Consequences

### Positive

- High confidence in changes
- Regression protection

### Negative

- Increased test maintenance