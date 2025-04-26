package com.almonium.infra.messaging.rabbit;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.config.properties.RabbitMQProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures RabbitMQ Exchanges, Queues, and Bindings based on application properties.
 * Includes setup for Dead Letter Queues (DLQ).
 */
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RabbitMQConfig {
    RabbitMQProperties rabbitMQProperties;
    ObjectMapper objectMapper;

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    // 1. Declare the main Events Exchange
    @Bean
    public TopicExchange eventsExchange() {
        return new TopicExchange(rabbitMQProperties.getExchange().getEvents(), true, false);
    }

    // 2. Declare the Dead Letter Exchange
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(rabbitMQProperties.getExchange().getDlx(), true, false);
    }

    // 3a. Declare the Dead Letter Queue for User Stream Setup
    @Bean
    public Queue userStreamSetupDlq() {
        return QueueBuilder.durable(getUserStreamSetupProps().getDlqName()).build();
    }

    // 4a. Bind the User Stream Setup DLQ to the Dead Letter Exchange
    @Bean
    public Binding userStreamSetupDlqBinding(Queue userStreamSetupDlq, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(userStreamSetupDlq)
                .to(deadLetterExchange)
                .with(getUserStreamSetupProps().getDlqName());
    }

    // 5a. Declare the main Queue for User Stream Setup
    @Bean
    public Queue userStreamSetupQueue() {
        return QueueBuilder.durable(getUserStreamSetupProps().getName())
                .withArgument(
                        "x-dead-letter-exchange",
                        rabbitMQProperties.getExchange().getDlx())
                .withArgument(
                        "x-dead-letter-routing-key", getUserStreamSetupProps().getName())
                .build();
    }

    // 6a. Bind the main User Stream Setup Queue to the main Events Exchange
    @Bean
    public Binding userStreamSetupBinding(Queue userStreamSetupQueue, TopicExchange eventsExchange) {
        return BindingBuilder.bind(userStreamSetupQueue)
                .to(eventsExchange)
                .with(getUserStreamSetupProps().getRoutingKey()); // Use routing key from properties
    }

    private RabbitMQProperties.QueueDetails getUserStreamSetupProps() {
        return rabbitMQProperties.getQueue().getUserStreamSetup();
    }
}
