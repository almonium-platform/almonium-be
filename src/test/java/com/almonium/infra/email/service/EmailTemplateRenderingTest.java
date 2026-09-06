package com.almonium.infra.email.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.almonium.config.properties.AppProperties;
import com.almonium.infra.email.dto.EmailDto;
import com.almonium.infra.email.model.dto.EmailContext;
import com.almonium.infra.email.model.enums.AuthEmailTemplateType;
import com.almonium.subscription.model.entity.PlanSubscription;
import com.almonium.user.relationship.model.enums.FriendshipEvent;
import java.util.Map;
import java.util.stream.Stream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * Renders every send through the real Thymeleaf templates and CSS inliner and checks the inbox contract: a distinct
 * subject and preheader per send, no greeting line, one sentence-case action, and an unsubscribe link on social mail
 * only.
 */
class EmailTemplateRenderingTest {
    private static final String WEB_DOMAIN = "https://almonium.example";
    private static final String COUNTERPART = "marta";
    private static final String SHELL = "#2C2530";
    private static final String CARD = "#F9F6F5";

    private final EmailService emailService = mock(EmailService.class);
    private final AppProperties properties = properties();
    private final SpringTemplateEngine templateEngine = templateEngine();

    private final AuthEmailComposerService authComposer =
            new AuthEmailComposerService(emailService, templateEngine, properties);
    private final FriendshipEmailComposerService friendshipComposer =
            new FriendshipEmailComposerService(emailService, templateEngine, properties);
    private final SubscriptionEmailComposerService subscriptionComposer =
            new SubscriptionEmailComposerService(emailService, templateEngine, properties);

    static Stream<Arguments> sends() {
        return Stream.of(
                Arguments.of(
                        "auth/email-verification",
                        "Verify your email address",
                        "Confirm this address to finish setting up your account.",
                        "Verify email",
                        false),
                Arguments.of(
                        "auth/password-reset",
                        "Reset your password",
                        "If you didn't ask for this, ignore it — nothing changes.",
                        "Reset password",
                        false),
                Arguments.of(
                        "auth/email-change",
                        "Confirm your email change",
                        "Your current sign-in keeps working until you confirm.",
                        "Verify and update email",
                        false),
                Arguments.of(
                        "friendship/initiated",
                        "@marta wants to connect",
                        "Accept, ignore, or hide your profile. Ignoring sends no notification.",
                        "View profile",
                        true),
                Arguments.of(
                        "friendship/accepted",
                        "@marta accepted your request",
                        "Their profile is open to you now.",
                        "View profile",
                        true),
                Arguments.of(
                        "subscription/created",
                        "Your membership is active",
                        "Every level-adapted edition, private imports, and unlimited saved words.",
                        "Open Almonium",
                        false),
                Arguments.of(
                        "subscription/renewed",
                        "Your subscription renewed",
                        "Nothing to do. Billing and cancellation live in Settings.",
                        "Open Almonium",
                        false),
                Arguments.of(
                        "subscription/payment-failed",
                        "We couldn't take your payment",
                        "Premium is still on. Update your card to avoid an interruption.",
                        "Update payment details",
                        false),
                Arguments.of(
                        "subscription/cancelled",
                        "Your subscription is cancelled",
                        "Access continues to the end of the current billing period.",
                        "Open Settings",
                        false),
                Arguments.of(
                        "subscription/ended",
                        "Your subscription has ended",
                        "Premium access is off. You can upgrade again any time.",
                        "Upgrade now",
                        false),
                Arguments.of(
                        "subscription/reactivated",
                        "Your subscription is active again",
                        "Level-adapted editions and private imports are open again.",
                        "Open Almonium",
                        false));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("sends")
    void everySendOwnsItsInboxRow(
            String send, String subject, String preheader, String action, boolean unsubscribable) {
        EmailDto email = render(send, Map.of());
        Document html = Jsoup.parse(email.body());

        assertThat(email.subject()).isEqualTo(subject);

        Element firstNode = html.body().children().first();
        assertThat(firstNode).isNotNull();
        assertThat(firstNode.hasClass("preheader"))
                .as("preheader must precede the header so clients do not scrape the wordmark alt")
                .isTrue();
        assertThat(firstNode.text()).isEqualTo(preheader);
        assertThat(firstNode.attr("style")).contains("display:none");

        assertThat(html.body().text()).doesNotContain("Hello", "friendship");
        assertThat(html.select("td[bgcolor=#5A1A74] a").text()).isEqualTo(action);

        assertThat(html.select("a:contains(Unsubscribe)").hasText()).isEqualTo(unsubscribable);
        if (unsubscribable) {
            assertThat(html.select("a:contains(Unsubscribe)").attr("href")).isEqualTo(WEB_DOMAIN + "/settings/app");
            assertThat(html.select("a:contains(Settings)").attr("href")).isEqualTo(WEB_DOMAIN + "/settings");
        }

        // The text part opens on the first visible sentence, never on the hidden preview block or its padding.
        assertThat(email.textBody())
                .doesNotStartWith(preheader)
                .doesNotContain("\u200B")
                .contains(action);
    }

    /**
     * Outlook's dark mode recolours an email it believes is a light document, which turned the shell mauve and the card
     * charcoal. The declared colour scheme asks it not to, and the surviving stylesheet takes the colours back from the
     * older rewriter that recolours anyway.
     */
    @ParameterizedTest(name = "{0}")
    @MethodSource("sends")
    void everySendHoldsItsColoursInOutlookDarkMode(String send) {
        String body = render(send, Map.of()).body();
        Document html = Jsoup.parse(body);

        assertThat(html.select("meta[name=color-scheme]").attr("content")).isEqualTo("light dark");
        assertThat(html.select("meta[name=supported-color-schemes]").attr("content"))
                .isEqualTo("light dark");

        Element embedded = html.selectFirst("head style[data-embed]");
        assertThat(embedded)
                .as("the dark-mode rules select elements that do not exist at render time, so they cannot be inlined")
                .isNotNull();
        assertThat(embedded.data()).contains(":root", "color-scheme: light dark", "[data-ogsb]", "[data-ogsc]");
        assertThat(html.select("style:not([data-embed])"))
                .as("every other block is inlined and dropped")
                .isEmpty();

        // Clients that strip CSS still need the ground painted, so the shell and the card carry it as an attribute.
        assertThat(html.body().attr("bgcolor")).isEqualTo(SHELL);
        assertThat(html.select(".header-cell").attr("bgcolor")).isEqualTo(SHELL);
        assertThat(html.select(".footer-cell").attr("bgcolor")).isEqualTo(SHELL);
        assertThat(html.select(".email-container").attr("bgcolor")).isEqualTo(CARD);
        assertThat(html.select(".email-content").attr("bgcolor")).isEqualTo(CARD);

        // A blocked wordmark falls back to alt text, which sits on the dark shell and has to be light.
        assertThat(html.select("img.header-wordmark").attr("style")).contains("color: #F9F6F5");

        assertThat(html.select("[style]").stream().map(e -> e.attr("style")))
                .as("the inliner strips CSS comments instead of pasting them into style attributes")
                .noneMatch(style -> style.contains("/*"));
    }

    @Test
    void receiptsCarryTheRenewalDateWhenItIsAhead() {
        Map<String, String> period = Map.of(SubscriptionEmailComposerService.PERIOD_ENDS_AT, "2031-09-12T00:00:00Z");

        assertThat(render("subscription/created", period).body()).contains("Renews on", "12 September 2031");
        assertThat(render("subscription/renewed", period).body()).contains("Next renewal", "12 September 2031");
        assertThat(render("subscription/reactivated", period).body()).contains("Renews on", "12 September 2031");
        assertThat(render("subscription/cancelled", period).body())
                .contains("Premium access continues until", "12 September 2031");
    }

    @Test
    void receiptsFallBackToProseWithoutAPeriodEnd() {
        assertThat(render("subscription/created", Map.of()).body()).doesNotContain("Renews on");
        assertThat(render("subscription/renewed", Map.of()).body()).doesNotContain("Next renewal");
        assertThat(render("subscription/cancelled", Map.of()).body())
                .contains("continues to the end of the current billing period");
    }

    @Test
    void emailChangeNamesTheAddressBeingConfirmed() {
        assertThat(render("auth/email-change", Map.of()).body()).contains("new@example.com");
    }

    private EmailDto render(String send, Map<String, String> extraAttributes) {
        String[] parts = send.split("/");
        String subfolder = parts[0];
        String template = parts[1];
        switch (subfolder) {
            case "auth" ->
                authComposer.sendEmail(
                        "there",
                        "new@example.com",
                        new EmailContext<>(
                                authType(template),
                                Map.of(AuthEmailComposerService.ACTION_URL, WEB_DOMAIN + "/verify-email?oobCode=abc")));
            case "friendship" ->
                friendshipComposer.sendEmail(
                        "oleg",
                        "oleg@example.com",
                        new EmailContext<>(
                                FriendshipEvent.valueOf(template.toUpperCase()),
                                Map.of(FriendshipEmailComposerService.COUNTERPART_USERNAME, COUNTERPART)));
            case "subscription" -> {
                Map<String, String> attributes = new java.util.HashMap<>(extraAttributes);
                attributes.put(SubscriptionEmailComposerService.PLAN_NAME, "PREMIUM");
                subscriptionComposer.sendEmail(
                        "oleg", "oleg@example.com", new EmailContext<>(subscriptionEvent(template), attributes));
            }
            default -> throw new IllegalArgumentException(send);
        }
        ArgumentCaptor<EmailDto> sent = ArgumentCaptor.forClass(EmailDto.class);
        verify(emailService, org.mockito.Mockito.atLeastOnce()).sendEmail(sent.capture());
        return sent.getValue();
    }

    private static AuthEmailTemplateType authType(String template) {
        return AuthEmailTemplateType.valueOf(template.toUpperCase().replace('-', '_'));
    }

    private static PlanSubscription.Event subscriptionEvent(String template) {
        return switch (template) {
            case "cancelled" -> PlanSubscription.Event.CANCELED;
            case "payment-failed" -> PlanSubscription.Event.PAYMENT_FAILED;
            default -> PlanSubscription.Event.valueOf(template.toUpperCase());
        };
    }

    private static AppProperties properties() {
        AppProperties properties = new AppProperties();
        properties.setWebDomain(WEB_DOMAIN);
        return properties;
    }

    private static SpringTemplateEngine templateEngine() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("email-templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(resolver);
        return engine;
    }
}
