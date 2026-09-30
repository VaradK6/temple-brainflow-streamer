# Temple BrainFlow — Real-Time Biometric Telemetry Streamer

A high-performance, non-blocking telemetry ingestion and live streaming engine designed for continuous cerebral hemodynamics and autonomic entropy tracking. Built with Java 21, Spring Boot 3, Spring WebFlux, and Project Reactor.

## Architecture Highlights
- **Reactive Non-Blocking Engine:** Uses Spring WebFlux over Netty event loops to maintain continuous Server-Sent Events (SSE) streams without thread starvation.
- **Dual-Channel Telemetry Pipeline:** Exposes an asynchronous `/api/v1/telemetry/ingest` HTTP gateway that broadcasts live biosensor packets through an in-memory `Sinks.Many` hub, merged with a deterministic hemodynamic baseline wave generator.
- **Zero-Dependency Real-Time Dashboard:** An embedded dark-mode dashboard served directly from resources via native browser `EventSource` and Chart.js with client-side sliding window buffering.

## Getting Started

### Prerequisites
- JDK 21+
- Maven wrapper (included)

### Running Locally
```bash
./mvnw clean spring-boot:run