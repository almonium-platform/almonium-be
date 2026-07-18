package com.almonium.subscription.webhook;

import com.almonium.subscription.exception.StripeIntegrationException;
import com.stripe.model.Event;
import org.springframework.stereotype.Component;

@Component
public class StripeEventObjectExtractor {
    public <T> T extract(Event event, Class<T> expectedType) {
        Object eventObject = event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new StripeIntegrationException(expectedType.getSimpleName() + " object is null"));

        if (!expectedType.isInstance(eventObject)) {
            throw new StripeIntegrationException(
                    "Expected " + expectedType.getSimpleName() + " for event " + event.getType() + " but received "
                            + eventObject.getClass().getSimpleName());
        }
        return expectedType.cast(eventObject);
    }
}
