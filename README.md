# 🧠 Temple BrainFlow™ — Reactive Biometric Telemetry Ingestion Engine

[![Java 21](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot 3](https://img.shields.io/badge/Spring_Boot_3.3-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring WebFlux](https://img.shields.io/badge/Spring_WebFlux-Reactive-blue?style=for-the-badge)](https://projectreactor.io/)
[![License](https://img.shields.io/badge/Architecture-Event--Driven-teal?style=for-the-badge)](#)

<p align="center">
  <img src="docs/demo.gif" alt="Temple BrainFlow Live Telemetry Console" width="850"/>
</p>

> A high-throughput, non-blocking telemetry ingestion and live streaming engine designed for continuous cerebral hemodynamics and autonomic entropy tracking. Built from first principles to demonstrate zero-thread-starvation biosensor ingestion for health wearables.
---

## ⚡ The Engineering Problem: Wearable Telemetry at Scale

Continuous forehead wearables tracking prefrontal hemodynamics stream uninterrupted biosignals (pulse transit time, perfusion, inertial data).

* **The Traditional Bottleneck (Spring MVC / Tomcat):** Allocates a dedicated thread per connection (~1MB stack memory). Maintaining continuous live connections for 10,000 active devices causes thread starvation, high context switching, and server crashes.
* **The First-Principles Solution (Spring WebFlux / Netty):** Replaces thread-per-request blocking architectures with a reactive non-blocking event-loop model. A handful of event loops manage thousands of persistent telemetry streams via Server-Sent Events (SSE) with minimal memory footprint.

---

## 🏗️ System Architecture

```text
 [Forehead Wearable / Companion App]
                  │
                  │  POST /api/v1/telemetry/ingest (HTTP Gateway)
                  ▼
      ┌───────────────────────┐
      │  Spring WebFlux Node  │ ◄── [Mathematical Wave Generator]
      │  (Netty Event Loops)  │     (Simulates Mayer & Traube Waves)
      └───────────┬───────────┘
                  │
                  │  Sinks.Many<T> Broadcast Hub (Zero-Allocation)
                  ▼
         [Flux.merge() Engine]
                  │
                  │  GET /api/v1/telemetry/stream (SSE 1Hz Push)
                  ▼
     [Real-Time Medical Console]
     (Native EventSource + Chart.js + Sliding Window Buffer)
```

### Core Architecture Highlights

* **Dual-Channel Telemetry Pipeline:** Exposes an asynchronous `/api/v1/telemetry/ingest` HTTP gateway that broadcasts live biosensor bursts through an in-memory `Sinks.Many` hub, seamlessly merged with deterministic baseline wave generators.
* **Physiological Signal Modeling:** Mathematically models the inverse relationship between Pulse Transit Time (PTT) and sympathetic arterial tone, synthesizing Autonomic Entropy to classify cognitive states (`OPTIMAL`, `ELEVATED_STRAIN`, `CEREBRAL_FATIGUE`).
* **Memory-Safe Sliding Window Buffer:** Client canvas uses an O(1) rolling shift queue (clamped to the latest 25 readings), eliminating memory leaks during continuous streaming sessions.
* **Zero-Dependency Deployment:** Embedded dark-mode telemetry dashboard served directly from resources without requiring Node.js or npm build chains.

---

## 🚀 Getting Started

### Prerequisites

* JDK 21+
* Git

### Run Locally

```bash
# Clone the repository
git clone https://github.com/VaradK6/temple-brainflow-streamer.git
cd temple-brainflow-streamer

# Run via Maven Wrapper
./mvnw clean spring-boot:run
```

(On Windows: `.\mvnw.cmd clean spring-boot:run`)

Open [http://localhost:8080](http://localhost:8080) in your browser to view the real-time diagnostic console.

---

## 🧪 Ingestion Testing

### Option A: Using the Built-in Console Button

Click the **"⚡ Simulate Fatigue Spike"** button in the top-right corner of the dashboard to trigger an immediate ingestion event and state transition.

### Option B: External Packet Ingestion via PowerShell

Simulate a companion mobile app sending a raw sensor burst:

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/telemetry/ingest" `
  -Method Post `
  -ContentType "application/json" `
  -Body (@{
      timestamp = (Get-Date).ToUniversalTime().ToString("yyyy-MM-ddTHH:mm:ssZ")
      deviceId = "DEVICE-TMPL-882"
      cerebralPerfusionIndex = 44.2
      pulseTransitTimeMs = 188.0
      autonomicEntropyScore = 0.78
      heartRateBpm = 94
      state = "CEREBRAL_FATIGUE"
  } | ConvertTo-Json)
```

---

## 🔬 Domain Model & Physiology Mapping

| Biometric Parameter | Unit / Range | Physiological Significance |
| :--- | :--- | :--- |
| **Cerebral Perfusion Index** | 30.0 – 100.0 CPI | Microvascular blood flow rate across prefrontal cortex |
| **Pulse Transit Time (PTT)** | 180 – 260 ms | Vascular compliance marker; inversely tracks arterial stiffness and sympathetic strain |
| **Autonomic Entropy** | 0.05 – 1.00 | Quantitative metabolic strain vs. recovery equilibrium |
| **State Classifier** | Categorical | Threshold evaluation: `OPTIMAL` (< 0.45), `ELEVATED_STRAIN`, `CEREBRAL_FATIGUE` (> 0.70) |