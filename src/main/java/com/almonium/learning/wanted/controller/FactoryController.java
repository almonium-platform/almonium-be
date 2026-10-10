package com.almonium.learning.wanted.controller;

import com.almonium.learning.book.controller.internal.BooksSignature;
import com.almonium.learning.book.dto.response.ProcessorIngestStatus;
import com.almonium.learning.wanted.dto.request.FactoryReservationRequest;
import com.almonium.learning.wanted.dto.response.FactoryQueueRow;
import com.almonium.learning.wanted.dto.response.FactoryReservation;
import com.almonium.learning.wanted.service.FactoryService;
import jakarta.validation.Validator;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

/**
 * The factory's side of the internal books surface, signed like a publication. Every call is a POST so one signing
 * scheme covers them all: the queue (an empty body), a reservation, and the progress of a reserved preparation.
 */
@RestController
@RequestMapping("/internal/books/factory")
@RequiredArgsConstructor
public class FactoryController {
    private final FactoryService factoryService;
    private final BooksSignature signature;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    @PostMapping("/queue")
    public ResponseEntity<List<FactoryQueueRow>> queue(
            @RequestHeader("X-Almonium-Books-Timestamp") long timestamp,
            @RequestHeader("X-Almonium-Books-Signature") String signed,
            @RequestBody byte[] body) {
        signature.verify(timestamp, signed, body);
        return ResponseEntity.ok(factoryService.queue());
    }

    @PostMapping("/preparations")
    public ResponseEntity<FactoryReservation> reserve(
            @RequestHeader("X-Almonium-Books-Timestamp") long timestamp,
            @RequestHeader("X-Almonium-Books-Signature") String signed,
            @RequestBody byte[] body) {
        signature.verify(timestamp, signed, body);
        FactoryReservationRequest request = parse(body);
        return ResponseEntity.ok(factoryService.reserve(request));
    }

    @PostMapping("/preparations/{preparationId}/status")
    public ResponseEntity<Void> status(
            @PathVariable UUID preparationId,
            @RequestHeader("X-Almonium-Books-Timestamp") long timestamp,
            @RequestHeader("X-Almonium-Books-Signature") String signed,
            @RequestBody byte[] body) {
        signature.verify(timestamp, signed, body);
        JsonNode node;
        try {
            node = objectMapper.readTree(body);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Malformed status report", exception);
        }
        factoryService.report(preparationId, ProcessorIngestStatus.from(node));
        return ResponseEntity.noContent().build();
    }

    /** The body is read raw for the signature, so validation runs here, as for a publication. */
    private FactoryReservationRequest parse(byte[] body) {
        FactoryReservationRequest request;
        try {
            request = objectMapper.readValue(body, FactoryReservationRequest.class);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Malformed reservation request", exception);
        }
        String violations = validator.validate(request).stream()
                .sorted(Comparator.comparing(
                        violation -> violation.getPropertyPath().toString()))
                .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                .collect(Collectors.joining(", "));
        if (!violations.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid reservation request: " + violations);
        }
        return request;
    }
}
