package com.almonium.engine.client.chatgpt.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.engine.client.chatgpt.client.GptClient;
import com.almonium.engine.client.chatgpt.dto.request.GptRequest;
import com.almonium.engine.client.chatgpt.dto.response.GptResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class GptService {
    final GptClient gptClient;

    @Value("${openai.gpt.model}")
    String model;

    public String getChatResponse(String prompt) {
        GptRequest gptRequest = new GptRequest(model, prompt);
        GptResponse gptResponse = gptClient.chat(gptRequest);

        if (gptResponse.choices().isEmpty()) {
            return "No response";
        }

        return gptResponse.choices().get(0).message().content();
    }
}
