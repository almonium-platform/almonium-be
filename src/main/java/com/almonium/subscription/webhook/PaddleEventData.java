package com.almonium.subscription.webhook;

import com.almonium.subscription.exception.PaddleIntegrationException;
import com.fasterxml.jackson.databind.JsonNode;
import java.time.Instant;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class PaddleEventData {
    public String requiredText(JsonNode data, String pointer) {
        JsonNode value = data.at(pointer);
        if (!value.isTextual() || value.textValue().isBlank()) {
            throw new PaddleIntegrationException("Paddle webhook is missing " + pointer);
        }
        return value.textValue();
    }

    public Instant requiredInstant(JsonNode data, String pointer) {
        try {
            return Instant.parse(requiredText(data, pointer));
        } catch (RuntimeException exception) {
            throw new PaddleIntegrationException("Paddle webhook has an invalid timestamp at " + pointer, exception);
        }
    }

    public Optional<Integer> foundingMemberSlot(JsonNode data) {
        JsonNode value = data.at("/custom_data/founding_member_slot");
        if (value.isMissingNode() || value.isNull()) {
            return Optional.empty();
        }
        if (!value.canConvertToInt()) {
            throw new PaddleIntegrationException("Paddle webhook has an invalid founding-member slot");
        }
        return Optional.of(value.intValue());
    }
}
