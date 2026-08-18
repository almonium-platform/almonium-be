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

    public Optional<Instant> optionalInstant(JsonNode data, String pointer) {
        JsonNode value = data.at(pointer);
        if (value.isMissingNode() || value.isNull()) {
            return Optional.empty();
        }
        if (!value.isTextual()) {
            throw new PaddleIntegrationException("Paddle webhook has an invalid timestamp at " + pointer);
        }
        try {
            return Optional.of(Instant.parse(value.textValue()));
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

    public Optional<String> scheduledChangeAction(JsonNode data) {
        JsonNode scheduledChange = data.get("scheduled_change");
        if (scheduledChange == null || scheduledChange.isNull()) {
            return Optional.empty();
        }
        JsonNode action = scheduledChange.get("action");
        if (action == null || !action.isTextual() || action.textValue().isBlank()) {
            throw new PaddleIntegrationException("Paddle webhook has an invalid scheduled change action");
        }
        return Optional.of(action.textValue());
    }
}
