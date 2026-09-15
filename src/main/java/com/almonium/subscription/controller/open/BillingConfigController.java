package com.almonium.subscription.controller.open;

import com.almonium.config.properties.PaddleProperties;
import com.almonium.subscription.dto.response.PaddleCheckoutConfigDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/billing")
@RequiredArgsConstructor
public class BillingConfigController {
    private final PaddleProperties properties;

    @GetMapping("/config")
    public PaddleCheckoutConfigDto config() {
        return new PaddleCheckoutConfigDto(
                properties.getClientToken(),
                properties.getEnvironment() == PaddleProperties.Environment.SANDBOX ? "sandbox" : "production");
    }
}
