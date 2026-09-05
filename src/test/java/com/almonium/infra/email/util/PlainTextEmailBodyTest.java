package com.almonium.infra.email.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PlainTextEmailBodyTest {

    @Test
    void includesReadableTextAndLinkTargets() {
        String plainText =
                PlainTextEmailBody.fromHtml("<p>Hello</p><a href=\"https://example.com/profile\">View profile</a>");

        assertThat(plainText)
                .contains("Hello", "View profile: example.com/profile", "Links:")
                .doesNotContain("https://example.com/profile");
    }

    @Test
    void dropsTheHiddenPreheaderAndItsZeroWidthPadding() {
        String plainText = PlainTextEmailBody.fromHtml(
                """
                <body>
                <div class="preheader" style="display:none"><span>Preview only.</span>&#8203;&#8203;&#8203;</div>
                <p>Body text.</p>
                </body>""");

        assertThat(plainText).isEqualTo("Body text.").doesNotContain("\u200B", "Preview only.");
    }
}
