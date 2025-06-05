package com.almonium.analyzer.client.chatgpt.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.client.chatgpt.client.GptClient;
import com.almonium.analyzer.client.chatgpt.dto.request.GptRequest;
import com.almonium.analyzer.client.chatgpt.dto.response.GptResponse;
import com.almonium.config.properties.AiProperties;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class GptService {
    GptClient gptClient;

    AiProperties aiProperties;

    public String getChatResponse(String prompt) {
        GptRequest gptRequest = new GptRequest(aiProperties.getGpt().getModel(), prompt);
        GptResponse gptResponse = gptClient.chat(gptRequest);

        if (gptResponse.choices().isEmpty()) {
            return "No response";
        }

        return gptResponse.choices().get(0).message().content();
    }
}
