package com.almonium.subscription.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.almonium.config.properties.PaddleProperties;
import com.almonium.subscription.model.entity.Plan;
import org.junit.jupiter.api.Test;

class PaddlePriceCatalogTest {
    @Test
    void mapsRegularAndFounderPricesToTheSamePlanInterval() {
        PaddlePriceCatalog catalog = new PaddlePriceCatalog(properties());
        catalog.validate();

        assertThat(catalog.priceIdFor(Plan.Type.MONTHLY, false)).isEqualTo("pri_regular_monthly");
        assertThat(catalog.priceIdFor(Plan.Type.MONTHLY, true)).isEqualTo("pri_founder_monthly");
        assertThat(catalog.planTypeFor("pri_founder_annual")).isEqualTo(Plan.Type.YEARLY);
        assertThat(catalog.isFounderPrice("pri_founder_annual")).isTrue();
    }

    @Test
    void rejectsDuplicatePriceIdsAtStartup() {
        PaddleProperties properties = properties();
        properties.getPrices().setFounderAnnual("pri_regular_monthly");

        assertThatThrownBy(() -> new PaddlePriceCatalog(properties).validate())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("unique");
    }

    private PaddleProperties properties() {
        PaddleProperties properties = new PaddleProperties();
        properties.getPrices().setPremiumMonthly("pri_regular_monthly");
        properties.getPrices().setPremiumAnnual("pri_regular_annual");
        properties.getPrices().setFounderMonthly("pri_founder_monthly");
        properties.getPrices().setFounderAnnual("pri_founder_annual");
        return properties;
    }
}
