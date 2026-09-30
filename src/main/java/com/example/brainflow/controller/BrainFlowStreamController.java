package com.example.brainflow.controller;

import com.example.brainflow.model.BrainFlowTelemetry;
import com.example.brainflow.service.BrainFlowStreamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/telemetry")
@CrossOrigin(origins = "*")
public class BrainFlowStreamController {

    private final BrainFlowStreamService streamService;

    public BrainFlowStreamController(BrainFlowStreamService streamService) {
        this.streamService = streamService;
    }

    @PostMapping("/ingest")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void ingestTelemetry(@RequestBody BrainFlowTelemetry packet) {
        streamService.emitTelemetry(packet);
    }

    @GetMapping(value = "/stream/{deviceId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<BrainFlowTelemetry>> streamBrainFlow(@PathVariable String deviceId) {
        return streamService.getTelemetryStream()
                .map(data -> ServerSentEvent.<BrainFlowTelemetry>builder()
                        .id(String.valueOf(data.timestamp().toEpochMilli()))
                        .event("brainflow-packet")
                        .data(data)
                        .build());
    }
}