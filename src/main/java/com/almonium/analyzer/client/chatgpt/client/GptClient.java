package com.almonium.analyzer.client.chatgpt.client;

import com.almonium.analyzer.client.chatgpt.dto.request.GptRequest;
import com.almonium.analyzer.client.chatgpt.dto.response.GptResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface GptClient {
    @PostExchange
    GptResponse chat(@RequestBody GptRequest request);
}
