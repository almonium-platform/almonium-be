package com.almonium.subscription.service;

import com.almonium.config.properties.PaddleProperties;
import com.almonium.subscription.exception.PaddleIntegrationException;
import com.almonium.subscription.model.entity.Plan;
import jakarta.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaddlePriceCatalog {
    private final PaddleProperties properties;

    @PostConstruct
    void validate() {
        List<String> priceIds = allPriceIds();
        if (priceIds.stream().anyMatch(priceId -> !priceId.startsWith("pri_"))) {
            throw new IllegalStateException("Paddle price IDs must start with pri_");
        }
        Set<String> uniqueIds = new HashSet<>(priceIds);
        if (uniqueIds.size() != priceIds.size()) {
            throw new IllegalStateException("Paddle price IDs must be unique");
        }
    }

    public String priceIdFor(Plan.Type type, boolean founder) {
        PaddleProperties.Prices prices = properties.getPrices();
        return switch (type) {
            case MONTHLY -> founder ? prices.getFounderMonthly() : prices.getPremiumMonthly();
            case YEARLY -> founder ? prices.getFounderAnnual() : prices.getPremiumAnnual();
            case LIFETIME -> throw new IllegalArgumentException("Lifetime plans do not have a Paddle price");
        };
    }

    public Plan.Type planTypeFor(String priceId) {
        PaddleProperties.Prices prices = properties.getPrices();
        if (priceId.equals(prices.getPremiumMonthly()) || priceId.equals(prices.getFounderMonthly())) {
            return Plan.Type.MONTHLY;
        }
        if (priceId.equals(prices.getPremiumAnnual()) || priceId.equals(prices.getFounderAnnual())) {
            return Plan.Type.YEARLY;
        }
        throw new PaddleIntegrationException("Unknown Paddle price ID");
    }

    public boolean isFounderPrice(String priceId) {
        return priceId.equals(properties.getPrices().getFounderMonthly())
                || priceId.equals(properties.getPrices().getFounderAnnual());
    }

    private List<String> allPriceIds() {
        PaddleProperties.Prices prices = properties.getPrices();
        return List.of(
                prices.getPremiumMonthly(),
                prices.getPremiumAnnual(),
                prices.getFounderMonthly(),
                prices.getFounderAnnual());
    }
}
