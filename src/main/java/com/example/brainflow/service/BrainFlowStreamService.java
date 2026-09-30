package com.example.brainflow.service;

import com.example.brainflow.model.BrainFlowTelemetry;
import com.example.brainflow.model.BrainFlowTelemetry.BrainState;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class BrainFlowStreamService {

    private final AtomicLong tick = new AtomicLong(0);
    // Shared multicast sink with replay so every connected SSE client receives updates
    private final Sinks.Many<BrainFlowTelemetry> telemetrySink =
            Sinks.many().multicast().directBestEffort();

    public BrainFlowStreamService() {
        // Background ticker emitting regular waves every 1 second
        Flux.interval(Duration.ofSeconds(1))
                .map(seq -> generateSample("DEVICE-TMPL-882"))
                .subscribe(telemetrySink::tryEmitNext);
    }

    public Flux<BrainFlowTelemetry> getTelemetryStream() {
        return telemetrySink.asFlux();
    }

    public void emitTelemetry(BrainFlowTelemetry packet) {
        telemetrySink.tryEmitNext(packet);
    }

    private BrainFlowTelemetry generateSample(String deviceId) {
        long t = tick.incrementAndGet();
        ThreadLocalRandom rnd = ThreadLocalRandom.current();

        double baseWave = 75.0 + 12.0 * Math.sin(t * 0.15);
        double jitter = rnd.nextDouble(-2.0, 2.0);
        double perfusion = Math.max(30.0, Math.min(100.0, baseWave + jitter));
        double ptt = 220.0 - (perfusion * 0.35) + rnd.nextDouble(-5.0, 5.0);
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