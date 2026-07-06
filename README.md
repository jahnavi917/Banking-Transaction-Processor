# Banking Transaction Processor — Architecture & Decisions

### Design Decisions & Narrative
**Rich Domain Model**: Business invariants (overdraft checks, validation patterns) are intentionally placed inside the `Account` aggregate instead of spilling into the service layer. This ensures domain integrity and highly maintainable test suites.
**Database & Persistence Choices**: For this time-boxed kata, Spring Data JPA with an H2 in-memory configuration was chosen to facilitate an out-of-the-box compilable application while honoring standard enterprise design frameworks.
**Concurrency Control (Race Conditions)**: If two threads attempt to withdraw from the same account simultaneously, a traditional web application could drift into an implicit overdraft state. We mitigated this by applying a explicit `PESSIMISTIC_WRITE` database lock on the account rows during transactional operations.
**Deadlock Elimination on Transfers**: In multi-account operations, if User A transfers to User B while User B simultaneously transfers to User A, an intersection deadlock can freeze the microservice thread pool. We solved this by sorting the resource locks alphabetically by Account ID before processing logic, establishing a strict deterministic execution order.
**Immutability and Scale**: Financial data sizes use `BigDecimal` rather than `double` or `float` to systematically avoid binary floating-point rounding discrepancies.
**Event Sourcing**: Rebuild the ledger layer using true Event Sourcing architecture (Kafka/Axon Framework) rather than basic relational rows, transforming updates into a stream of immutable financial occurrences.
**Idempotency Engine**: Introduce an idempotency key infrastructure on REST processing layers (`X-Idempotency-Key`) to securely handle duplicate client requests from dropping connections or retry timeouts.
