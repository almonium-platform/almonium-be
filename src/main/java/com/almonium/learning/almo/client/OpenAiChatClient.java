package com.almonium.learning.almo.client;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.client.exception.ApiIntegrationException;
import com.almonium.config.properties.AlmoProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;

/**
 * The one call that costs money. Chat Completions, JSON mode, no SDK: the request is four fields and the answer is
 * read with a JSON pointer, which is less to keep in step than a client library would be.
 */
@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class OpenAiChatClient {
    RestTemplate restTemplate;
    AlmoProperties properties;

    /** A turn of the conversation as the model sees it: {@code system}, {@code user} or {@code assistant}. */
    public record Turn(String role, String content) {}

    /** The model's answer and what it cost, in the units the ledger records. */
    public record Completion(String content, String model, int promptTokens, int completionTokens) {}

    public boolean isConfigured() {
        String key = properties.getOpenAi().getApiKey();
        return key != null && !key.isBlank();
    }

    public Completion complete(String systemPrompt, List<Turn> history) {
        if (!isConfigured()) {
            throw new ApiIntegrationException("Almo has no model to speak with on this server");
        }

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", systemPrompt));
        history.forEach(turn -> messages.add(Map.of("role", turn.role(), "content", turn.content())));

        AlmoProperties.OpenAi openAi = properties.getOpenAi();
        Map<String, Object> body = Map.of(
                "model", openAi.getModel(),
                "max_completion_tokens", openAi.getMaxOutputTokens(),
                "response_format", Map.of("type", "json_object"),
                "messages", messages);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAi.getApiKey());

        try {
            JsonNode response =
                    restTemplate.postForObject(openAi.getUrl(), new HttpEntity<>(body, headers), JsonNode.class);
            if (response == null || response.at("/choices/0/message/content").isMissingNode()) {
                throw new ApiIntegrationException("The model answered without a message");
            }
            return new Completion(
                    response.at("/choices/0/message/content").asString(),
                    response.path("model").asString(openAi.getModel()),
                    response.at("/usage/prompt_tokens").asInt(0),
                    response.at("/usage/completion_tokens").asInt(0));
        } catch (RestClientException e) {
            throw new ApiIntegrationException("The model could not be reached: " + e.getMessage(), e);
        }
    }
}
