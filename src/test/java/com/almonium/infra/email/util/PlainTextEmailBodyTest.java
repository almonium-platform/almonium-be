package com.almonium.infra.email.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PlainTextEmailBodyTest {

    @Test
    void includesReadableTextAndLinkTargets() {
        String plainText =
                PlainTextEmailBody.fromHtml("<p>Hello</p><a href=\"https://example.com/profile\">View profile</a>");

        assertThat(plainText).contains("Hello", "View profile: https://example.com/profile", "Links:");
    }
}
