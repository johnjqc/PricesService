# ADR 0006 - Error Handling Strategy

## Status
Accepted

## Context

The application must expose consistent error responses
to API consumers.

## Decision

Adopt a centralized error handling strategy.

Elements:

- ApplicationError
- ErrorCode enumeration
- ControllerAdvice for REST exceptions
- Consistent API error responses

## Consequences

### Positive

- Consistent error responses
- Better API consumer experience

### Negative

- Additional abstraction layer