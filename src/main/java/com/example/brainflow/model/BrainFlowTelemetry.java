package com.example.brainflow.model;

import java.time.Instant;

public record BrainFlowTelemetry(
        Instant timestamp,
        String deviceId,
        double cerebralPerfusionIndex,  // 0 - 100: Prefrontal hemodynamic blood flow
        double pulseTransitTimeMs,      // Vascular tone marker (approx. 180 - 260 ms)
        double autonomicEntropyScore,   // 0.0 - 1.0: Real-time sympathetic strain
        int heartRateBpm,               // 45 - 130 bpm
        BrainState state                // OPTIMAL, ELEVATED_STRAIN, CEREBRAL_FATIGUE
) {
    public enum BrainState {
        OPTIMAL,
        ELEVATED_STRAIN,
        CEREBRAL_FATIGUE
    }
}