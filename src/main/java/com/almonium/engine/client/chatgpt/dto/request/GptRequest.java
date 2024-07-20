package com.almonium.engine.client.chatgpt.dto.request;

import com.almonium.engine.client.chatgpt.dto.common.Message;
import java.util.List;

public record GptRequest(String model, List<Message> messages, int n, double temperature) {
    private static final int DEFAULT_NUMBER_OF_RESPONSES = 1;
    private static final double DEFAULT_TEMPERATURE = 1.0;
    private static final String DEFAULT_ROLE = "user";

    public GptRequest(String model, String prompt) {
        this(model, List.of(new Message(DEFAULT_ROLE, prompt)), DEFAULT_NUMBER_OF_RESPONSES, DEFAULT_TEMPERATURE);
    }
}
