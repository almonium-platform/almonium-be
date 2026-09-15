package com.almonium.subscription.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import tools.jackson.databind.JsonNode;

public record PaddleEvent(
        @JsonProperty("event_id") String eventId,
        @JsonProperty("event_type") String eventType,
        @JsonProperty("occurred_at") Instant occurredAt,
        JsonNode data) {}
