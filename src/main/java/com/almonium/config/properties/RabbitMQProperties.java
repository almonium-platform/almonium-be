package com.almonium.config.properties;

import static lombok.AccessLevel.PRIVATE;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "rabbitmq")
@FieldDefaults(level = PRIVATE)
public class RabbitMQProperties {

    @NotNull
    @Valid
    @NestedConfigurationProperty
    Exchange exchange = new Exchange();

    @NotNull
    @Valid
    @NestedConfigurationProperty
    Queue queue = new Queue(); // Contains properties for all queues

    @Getter
    @Setter
    @Validated
    @FieldDefaults(level = PRIVATE)
    public static class Exchange {
        @NotBlank
        String events;

        @NotBlank
        String dlx;
    }

    @Getter
    @Setter
    @Validated
    @FieldDefaults(level = PRIVATE)
    public static class Queue {
        @NotNull
        @Valid
        @NestedConfigurationProperty
        QueueDetails userStreamSetup = new QueueDetails();
    }

    // Reusable structure for queue details
    @Getter
    @Setter
    @Validated
    @FieldDefaults(level = PRIVATE)
    public static class QueueDetails {
        @NotBlank
        String name;

        @NotBlank
        String routingKey;

        @NotBlank
        String dlqName;
    }
}
