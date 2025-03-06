package com.almonium.util.uuid;

import com.github.f4b6a3.uuid.UuidCreator;
import java.util.EnumSet;
import java.util.UUID;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;

public class UuidV7Generator implements BeforeExecutionGenerator {

    @Override
    public UUID generate(
            SharedSessionContractImplementor session, Object entity, Object currentValue, EventType eventType) {
        return currentValue instanceof UUID ? (UUID) currentValue : UuidCreator.getTimeOrderedEpoch();
    }

    @Override
    public EnumSet<EventType> getEventTypes() {
        return EnumSet.of(EventType.INSERT);
    }
}
