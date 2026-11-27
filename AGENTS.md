# Repository instructions

## Project

- This is a Java 25, Spring Boot, Maven application using PostgreSQL, JPA, Flyway, and Spring Modulith.
- The application is a modular monolith for personal expense tracking and apartment down-payment savings.
- Keep changes within the requested scope. Do not introduce microservices, multi-user support, authentication, or AI features unless requested.

## Architecture

- Organize code by business module, such as `categories`, `expenses`, and `budgeting`.
- Each module owns its business rules and persistence.
- Modules may use another module's public API, but must not depend on its internal classes, repositories, or persistence entities.
- Keep controllers focused on HTTP concerns; put business rules in services.
- Avoid exposing JPA entities as API or cross-module contracts. Prefer request/response DTOs or immutable public views.
- Categories are shared reference data: Expense and Budgeting should refer to the same category identity rather than keep separate category lists.
- Prefer deactivating records that may be referenced by other data over deleting them.

## Data and persistence

- Use Flyway migrations for schema changes. Do not rely on Hibernate schema auto-update for application schema.
- Treat applied migrations as immutable; add a new migration for later schema changes.
- Store monetary values as exact decimals, not `float` or `double`.
- Phase one uses USD. Do not silently convert currencies.
- Use calendar dates for expenses and savings activity. Weeks start on Monday.
- Keep calculated totals derived from their source records rather than persisting duplicate values unless there is a clear reason.

## API and errors

- Validate required input and reject blank names or other invalid values explicitly.
- Use specific domain exceptions for expected conditions, such as invalid input, missing categories, and duplicate names.
- Map expected API errors to appropriate HTTP status codes using the existing global exception handler and `ProblemDetail` responses.
- Keep the database constraint as the final guard against duplicate category names; application checks alone cannot prevent concurrent duplicates.
- Do not expose SQL, database constraint details, stack traces, or other internal implementation details in API responses.

## Tests

- Add focused tests for new behavior, including relevant invalid-input and error cases.
- Name happy-path tests `test<ProductionMethod>` (for example, `testCreate`).
- Name error and edge-case tests with a concise condition and outcome (for example, `createBlankNameThrowsInvalidCategory`, `findMissingCategoryThrowsNotFound`).
- Test module behavior at the service layer; add persistence or controller tests when database mapping or HTTP behavior is part of the change.

## Build and formatting

- Use the Maven wrapper (`./mvnw`) for Maven tasks.
- Run the relevant tests and formatting checks for the change. Report checks that were not run rather than implying they passed.
- Follow the repository's configured Spotless rules once they are established; do not introduce a second formatter configuration.