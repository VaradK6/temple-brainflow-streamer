package com.example.brainflow.controller;

import com.example.brainflow.model.BrainFlowTelemetry;
import com.example.brainflow.service.BrainFlowStreamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@RestController
@RequestMapping("/api/v1/telemetry")
@CrossOrigin(origins = "*")
public class BrainFlowStreamController {

    private final BrainFlowStreamService streamService;
    // Broadcast sink: accepts data from external devices and pushes to SSE subscribers
    private final Sinks.Many<BrainFlowTelemetry> deviceSink =
            Sinks.many().multicast().onBackpressureBuffer();

    public BrainFlowStreamController(BrainFlowStreamService streamService) {
        this.streamService = streamService;
    }

    // 1. INGESTION ENDPOINT: Mobile app or IoT gateway POSTs data here
    @PostMapping("/ingest")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void ingestTelemetry(@RequestBody BrainFlowTelemetry packet) {
        deviceSink.tryEmitNext(packet);
    }

    // 2. LIVE SSE STREAM: Pushes real ingested packets, falling back to simulator if idle
    @GetMapping(value = "/stream/{deviceId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<BrainFlowTelemetry>> streamBrainFlow(@PathVariable String deviceId) {
        // Merges incoming physical packets with the fallback simulator
        return Flux.merge(deviceSink.asFlux(), streamService.streamLiveTelemetry(deviceId))
                .map(data -> ServerSentEvent.<BrainFlowTelemetry>builder()
                        .id(String.valueOf(data.timestamp().toEpochMilli()))
                        .event("brainflow-packet")
                        .data(data)
                        .build());
    }
}