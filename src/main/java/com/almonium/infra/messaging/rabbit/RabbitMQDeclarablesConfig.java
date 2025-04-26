package com.almonium.infra.messaging.rabbit;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.config.properties.RabbitMQProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarable;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures RabbitMQ Exchanges, Queues, and Bindings using Declarables
 * based on application properties. Includes setup for Dead Letter Queues (DLQ).
 */
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RabbitMQDeclarablesConfig {
    RabbitMQProperties rabbitMQProperties;
    ObjectMapper objectMapper;

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public Declarables amqpDeclarables() {
        List<Declarable> declarables = new ArrayList<>();

        // 1. Create Exchanges
        TopicExchange eventsExchange = createEventsExchange();
        DirectExchange dlx = createDeadLetterExchange();
        declarables.add(eventsExchange);
        declarables.add(dlx);

        // 2. Iterate over queue configurations defined in properties
        if (rabbitMQProperties.getQueue() != null) { // Check if the map exists
            for (Map.Entry<String, RabbitMQProperties.QueueDetails> entry :
                    rabbitMQProperties.getQueue().entrySet()) {
                // String logicalName = entry.getKey(); // e.g., "user-stream-setup" (useful for logging)
                RabbitMQProperties.QueueDetails props = entry.getValue();

                // 3. Create Queue, DLQ, and Bindings for each entry
                Queue queue = createQueue(props, dlx.getName());
                Queue dlq = createDlq(props);
                // Bind main queue to main exchange
                Binding binding = createBinding(queue, eventsExchange, props.getRoutingKey());
                // Bind DLQ to DLX
                Binding dlqBinding = createDlqBinding(dlq, dlx, props.getName()); // Use original queue name as DLQ key

                declarables.add(queue);
                declarables.add(dlq);
                declarables.add(binding);
                declarables.add(dlqBinding);
            }
        }

        return new Declarables(declarables);
    }

    private TopicExchange createEventsExchange() {
        return new TopicExchange(rabbitMQProperties.getExchange().getEvents(), true, false);
    }

    private DirectExchange createDeadLetterExchange() {
        return new DirectExchange(rabbitMQProperties.getExchange().getDlx(), true, false);
    }

    private Queue createQueue(RabbitMQProperties.QueueDetails queueProps, String dlxName) {
        return QueueBuilder.durable(queueProps.getName())
                .withArgument("x-dead-letter-exchange", dlxName)
                .withArgument("x-dead-letter-routing-key", queueProps.getName()) // Route based on original queue name
                .build();
    }

    private Queue createDlq(RabbitMQProperties.QueueDetails queueProps) {
        return QueueBuilder.durable(queueProps.getDlqName()).build();
    }

    private Binding createBinding(Queue queue, TopicExchange exchange, String routingKey) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    private Binding createDlqBinding(Queue dlq, DirectExchange dlx, String originalQueueName) {
        return BindingBuilder.bind(dlq).to(dlx).with(originalQueueName); // Bind DLQ using original queue name
    }
}
