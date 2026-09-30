package com.example.brainflow.service;

import com.example.brainflow.model.BrainFlowTelemetry;
import com.example.brainflow.model.BrainFlowTelemetry.BrainState;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class BrainFlowStreamService {

    private final AtomicLong tick = new AtomicLong(0);

    public Flux<BrainFlowTelemetry> streamLiveTelemetry(String deviceId) {
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> generateSample(deviceId));
    }

    private BrainFlowTelemetry generateSample(String deviceId) {
        long t = tick.incrementAndGet();
        ThreadLocalRandom rnd = ThreadLocalRandom.current();

        // Sinusoidal oscillation representing cerebral blood flow + respiratory rhythm
        double baseWave = 75.0 + 12.0 * Math.sin(t * 0.15);
        double jitter = rnd.nextDouble(-2.0, 2.0);
        double perfusion = Math.max(30.0, Math.min(100.0, baseWave + jitter));

        // Pulse transit time inversely correlates with sympathetic tension
        double ptt = 220.0 - (perfusion * 0.35) + rnd.nextDouble(-5.0, 5.0);

        // Autonomic Entropy calculation: higher when perfusion drops under high pulse
        double entropy = Math.min(1.0, Math.max(0.05, ((100.0 - perfusion) / 100.0) * 0.8 + rnd.nextDouble(0.0, 0.2)));

        BrainState state;
        if (entropy > 0.70) {
            state = BrainState.CEREBRAL_FATIGUE;
        } else if (entropy > 0.45) {
            state = BrainState.ELEVATED_STRAIN;
        } else {
            state = BrainState.OPTIMAL;
        }

        int hr = (int) (62 + (entropy * 30) + rnd.nextInt(-3, 4));

        return new BrainFlowTelemetry(
                Instant.now(),
                deviceId,
                Math.round(perfusion * 100.0) / 100.0,
                Math.round(ptt * 10.0) / 10.0,
                Math.round(entropy * 1000.0) / 1000.0,
                hr,
                state
        );
    }
}