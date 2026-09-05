package com.almonium.infra.email;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import com.almonium.infra.email.config.ThymeleafConfig;
import com.almonium.infra.email.model.dto.EmailContext;
import com.almonium.infra.email.service.AuthEmailComposerService;
import com.almonium.infra.email.service.EmailService;
import com.almonium.infra.email.service.FriendshipEmailComposerService;
import com.almonium.infra.email.service.SubscriptionEmailComposerService;
import com.almonium.subscription.model.entity.PlanSubscription;
import com.almonium.user.relationship.model.enums.FriendshipEvent;
import com.almonium.util.HtmlFileWriter;
import com.almonium.util.config.TestConfig;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestTemplate;

/**
 * Not named *Test on purpose: runs the real composer/Thymeleaf/CssInliner pipeline (dry-run,
 * no network, no DB/Kafka/Firebase) to render every email template to disk for a quick look in a
 * browser, so it must stay out of `mvn test`/CI. Run explicitly:
 *   ./mvnw test -Dtest=EmailTemplatePreviewGenerator
 * Output: temp/previews/*.html (gitignored).
 */
@SpringBootTest
@ContextConfiguration(
        classes = {
            TestConfig.class,
            ThymeleafConfig.class,
            EmailService.class,
            AuthEmailComposerService.class,
            HtmlFileWriter.class,
            FriendshipEmailComposerService.class,
            SubscriptionEmailComposerService.class
        })
@TestPropertySource(
        locations = "classpath:application.yaml",
        // real domain so the rendered wordmark/links actually resolve when opened in a browser
        properties = "app.web-domain=https://almonium.com")
class EmailTemplatePreviewGenerator {

    private static final Path RENDERED_EMAIL_PATH = Path.of("temp/rendered_email.html");
    private static final Path OUTPUT_DIR = Path.of("temp/previews");

    @Autowired
    FriendshipEmailComposerService friendshipEmailComposerService;

    @Autowired
    AuthEmailComposerService authEmailComposerService;

    @Autowired
    SubscriptionEmailComposerService subscriptionEmailComposerService;

    @MockitoBean
    RestTemplate restTemplate;

    @Test
    void generateAllPreviews() throws IOException {
        Files.createDirectories(OUTPUT_DIR);

        for (FriendshipEvent event : FriendshipEvent.values()) {
            EmailContext<FriendshipEvent> context = new EmailContext<>(
                    event, Map.of(FriendshipEmailComposerService.COUNTERPART_USERNAME, "martazielinska"));
            friendshipEmailComposerService.sendEmail("kuzanoleg", "preview@example.com", context);
            capture("friendship-" + event.name().toLowerCase() + ".html");
        }

        for (PlanSubscription.Event event : PlanSubscription.Event.values()) {
            EmailContext<PlanSubscription.Event> context = new EmailContext<>(
                    event,
                    Map.of(
                            SubscriptionEmailComposerService.PLAN_NAME,
                            "PREMIUM",
                            SubscriptionEmailComposerService.PERIOD_ENDS_AT,
                            Instant.now().plus(30, ChronoUnit.DAYS).toString()));
            subscriptionEmailComposerService.sendEmail("kuzanoleg", "preview@example.com", context);
            capture("subscription-" + event.name().toLowerCase() + ".html");
        }

        for (com.almonium.infra.email.model.enums.AuthEmailTemplateType type :
                com.almonium.infra.email.model.enums.AuthEmailTemplateType.values()) {
            authEmailComposerService.sendEmail(
                    "there",
                    "preview@example.com",
                    new EmailContext<>(
                            type,
                            Map.of(
                                    AuthEmailComposerService.ACTION_URL,
                                    "https://almonium.com/preview-link?oobCode=abc123")));
            capture("auth-" + type.name().toLowerCase() + ".html");
        }

        writeIndex();
    }

    private void writeIndex() throws IOException {
        List<String> files;
        try (var stream = Files.list(OUTPUT_DIR)) {
            files = stream.map(p -> p.getFileName().toString())
                    .filter(name -> name.endsWith(".html") && !name.equals("index.html"))
                    .sorted()
                    .toList();
        }
        String rows = files.stream()
                .map(f -> "<tr><td style=\"padding:6px 12px;font-family:monospace;\">" + f
                        + "</td><td style=\"padding:6px 12px;\"><a href=\"" + f
                        + "\" target=\"preview\">open</a></td></tr>")
                .collect(Collectors.joining("\n"));
        String html =
                """
                <!DOCTYPE html>
                <html><head><meta charset="utf-8"><title>Email previews</title></head>
                <body style="margin:0;font-family:sans-serif;display:flex;height:100vh;">
                <div style="width:260px;overflow:auto;border-right:1px solid #ddd;padding:8px;">
                <table>%s</table>
                </div>
                <iframe name="preview" style="flex:1;border:0;" src="%s"></iframe>
                </body></html>"""
                        .formatted(rows, files.isEmpty() ? "" : files.get(0));
        Files.writeString(OUTPUT_DIR.resolve("index.html"), html);
    }

    private void capture(String filename) throws IOException {
        Files.move(RENDERED_EMAIL_PATH, OUTPUT_DIR.resolve(filename), REPLACE_EXISTING);
    }
}
