package com.almonium.info.feedback.service;

import com.almonium.auth.common.service.CurrentMember;
import com.almonium.config.properties.AppProperties;
import com.almonium.info.feedback.dto.FeedbackRequest;
import com.almonium.infra.email.dto.EmailDto;
import com.almonium.infra.email.service.EmailService;
import com.almonium.infra.email.util.CssInliner;
import com.almonium.infra.email.util.PlainTextEmailBody;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.model.entity.User;
import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

/**
 * The landing page's question, "What did every language app get wrong for you?". Nothing is stored: each answer is
 * one notification to the founder's inbox, with the visitor's address as Reply-To so answering it is one click. The
 * visitor gets no mail, since a typed address has not been verified. When the send fails the caller hears it, because
 * the inbox is the only copy and the page keeps the text for another try.
 */
@Service
public class FeedbackService {
    static final int MAX_LINKS = 2;
    private static final int SUBJECT_LENGTH = 40;
    private static final Pattern LINK = Pattern.compile("(?i)https?://|www\\.");
    /** The founder reads these in Kyiv, so the time in the mail is Kyiv time. */
    private static final ZoneId INBOX_ZONE = ZoneId.of("Europe/Kyiv");

    private static final DateTimeFormatter SENT_AT = DateTimeFormatter.ofPattern("d MMMM, HH:mm", Locale.ENGLISH);

    private final EmailService emailService;
    private final SpringTemplateEngine templateEngine;
    private final CurrentMember currentMember;
    private final AppProperties appProperties;
    private final String inboxAddress;
    private final Clock clock;

    @Autowired
    public FeedbackService(
            EmailService emailService,
            SpringTemplateEngine templateEngine,
            CurrentMember currentMember,
            AppProperties appProperties,
            @Value("${app.feedback.address:hello@almonium.com}") String inboxAddress) {
        this(emailService, templateEngine, currentMember, appProperties, inboxAddress, Clock.systemUTC());
    }

    FeedbackService(
            EmailService emailService,
            SpringTemplateEngine templateEngine,
            CurrentMember currentMember,
            AppProperties appProperties,
            String inboxAddress,
            Clock clock) {
        this.emailService = emailService;
        this.templateEngine = templateEngine;
        this.currentMember = currentMember;
        this.appProperties = appProperties;
        this.inboxAddress = inboxAddress;
        this.clock = clock;
    }

    public void submit(FeedbackRequest request) {
        if (request.website() != null && !request.website().isBlank()) {
            return; // the honeypot: a bot filled the field no person sees; it is told nothing
        }
        String message = request.message().trim();
        if (message.length() < 10) {
            throw new BadUserRequestActionException("Write a little more, at least 10 characters.");
        }
        if (LINK.matcher(message).results().count() > MAX_LINKS) {
            throw new BadUserRequestActionException("Leave out the links, please.");
        }
        String email = blankToNull(request.email());
        User member = currentMember.find().orElse(null);
        emailService.sendEmail(compose(message, email, member, blankToNull(request.locale())));
    }

    private EmailDto compose(String message, String email, User member, String locale) {
        Context context = new Context();
        context.setVariable("preheader", preheader(email));
        context.setVariable("logoUrl", appProperties.getWebDomain() + "/email/wordmark-white.png");
        context.setVariable("message", message);
        context.setVariable("email", email);
        context.setVariable("sentAt", SENT_AT.format(ZonedDateTime.now(clock.withZone(INBOX_ZONE))));
        context.setVariable("browserLanguage", browserLanguage(locale));
        context.setVariable("signedInAs", member == null ? null : "@" + member.getUsername());
        String body = CssInliner.inlineCss(templateEngine.process("feedback/landing-question.html", context));
        return new EmailDto(inboxAddress, subject(message), body, PlainTextEmailBody.fromHtml(body), email);
    }

    /** The first words of the answer, so the inbox reads as a list of answers. */
    static String subject(String message) {
        String flat = message.replaceAll("\\s+", " ");
        if (flat.length() <= SUBJECT_LENGTH) {
            return "“" + flat + "”";
        }
        return "“" + flat.substring(0, SUBJECT_LENGTH).stripTrailing() + "…”";
    }

    private static String preheader(String email) {
        return email == null ? "Anonymous. No reply address." : "From " + email + ". Reply to answer them directly.";
    }

    private static String browserLanguage(String locale) {
        if (locale == null) {
            return null;
        }
        String name = Locale.forLanguageTag(locale).getDisplayLanguage(Locale.ENGLISH);
        return name.isBlank() ? null : name;
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
