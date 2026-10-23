package com.almonium.info.feedback.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.auth.common.service.CurrentMember;
import com.almonium.config.properties.AppProperties;
import com.almonium.info.feedback.dto.FeedbackRequest;
import com.almonium.infra.email.dto.EmailDto;
import com.almonium.infra.email.service.EmailService;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.model.entity.User;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

class FeedbackServiceTest {
    private static final String ANSWER =
            "I always wanted a tool that lets me keep reading the actual book while it notes the words I hesitate on.";

    EmailService email = mock(EmailService.class);
    CurrentMember currentMember = mock(CurrentMember.class);
    FeedbackService service;

    @BeforeEach
    void aService() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("email-templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(resolver);
        AppProperties properties = new AppProperties();
        properties.setWebDomain("https://almonium.example");
        when(currentMember.find()).thenReturn(Optional.empty());
        // 18:40 UTC on 2 October is 21:40 in Kyiv.
        Clock clock = Clock.fixed(Instant.parse("2026-10-02T18:40:00Z"), ZoneOffset.UTC);
        service = new FeedbackService(email, engine, currentMember, properties, "hello@almonium.com", clock);
    }

    @Test
    void anAnswerWithAnEmailReachesTheInboxWithTheVisitorAsReplyTo() {
        service.submit(new FeedbackRequest(ANSWER, "marta@example.com", "uk-UA", ""));

        EmailDto mail = sent();
        assertThat(mail.recipient()).isEqualTo("hello@almonium.com");
        assertThat(mail.replyTo()).isEqualTo("marta@example.com");
        assertThat(mail.subject()).isEqualTo("“I always wanted a tool that lets me keep…”");
        assertThat(mail.textBody())
                .contains("What they were missing")
                .contains(ANSWER)
                .contains("2 October, 21:40")
                .contains("browser in Ukrainian")
                .contains("Signed out")
                .contains("Reply to this email to answer them.")
                .contains("Sent from the landing page question");
        assertThat(mail.body()).contains("From marta@example.com. Reply to answer them directly.");
    }

    @Test
    void anAnonymousAnswerSaysSoAndOffersNoReply() {
        User member = new User();
        member.setUsername("marta");
        when(currentMember.find()).thenReturn(Optional.of(member));

        service.submit(new FeedbackRequest("Flashcards that feel like reading.", " ", null, null));

        EmailDto mail = sent();
        assertThat(mail.replyTo()).isNull();
        assertThat(mail.subject()).isEqualTo("“Flashcards that feel like reading.”");
        assertThat(mail.textBody())
                .contains("No email given")
                .contains("@marta")
                .doesNotContain("Reply to this email")
                .doesNotContain("browser in");
        assertThat(mail.body()).contains("Anonymous. No reply address.");
    }

    @Test
    void theMessageIsEscapedNotRendered() {
        service.submit(new FeedbackRequest("<script>alert(1)</script> and more words", null, null, null));

        assertThat(sent().body()).doesNotContain("<script>").contains("&lt;script&gt;");
    }

    @Test
    void aFilledHoneypotIsAcceptedAndDropped() {
        service.submit(new FeedbackRequest(ANSWER, null, null, "https://spam.example"));

        verify(email, never()).sendEmail(any());
    }

    @Test
    void moreThanTwoLinksIsRefused() {
        String spam = "see https://a.example and http://b.example and www.c.example";

        assertThatThrownBy(() -> service.submit(new FeedbackRequest(spam, null, null, null)))
                .isInstanceOf(BadUserRequestActionException.class);
        verify(email, never()).sendEmail(any());
    }

    @Test
    void whitespaceDoesNotCountTowardTheMinimum() {
        assertThatThrownBy(() -> service.submit(new FeedbackRequest("   short    ", null, null, null)))
                .isInstanceOf(BadUserRequestActionException.class);
    }

    private EmailDto sent() {
        ArgumentCaptor<EmailDto> mail = ArgumentCaptor.forClass(EmailDto.class);
        verify(email).sendEmail(mail.capture());
        return mail.getValue();
    }
}
