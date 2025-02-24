package com.almonium.analyzer.client.chatgpt.controller;

import com.almonium.analyzer.client.chatgpt.service.GptService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO integrate
@RestController
@RequestMapping
@RequiredArgsConstructor
public class ChatController {
    private final GptService gptService;

    @PostMapping("/public/chat")
    public String chat(@RequestBody String prompt) {
        return gptService.getChatResponse(prompt);
    }
}
