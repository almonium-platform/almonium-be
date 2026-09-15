package com.almonium.learning.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.infra.email.model.dto.EmailContext;
import com.almonium.infra.email.service.BookEmailComposerService;
import com.almonium.learning.book.model.enums.BookEmailType;
import com.almonium.user.core.model.entity.Profile;
import com.almonium.user.core.model.entity.User;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookEmailServiceTest {
    @Mock
    BookEmailComposerService composer;

    @InjectMocks
    BookEmailService service;

    @Test
    @SuppressWarnings("unchecked")
    void fulfilmentMailCarriesTheMonthThePathAndTheAllowanceLine() {
        User reader = user(true);

        service.translationReady(
                reader,
                "Effi Briest",
                Language.UK,
                "August",
                "/reader/effi-briest-de-original?parallel=UK",
                "You have 2 requests left this month.");

        ArgumentCaptor<EmailContext<BookEmailType>> context = ArgumentCaptor.forClass(EmailContext.class);
        verify(composer).sendEmail(eq("oleg"), eq("oleg@example.com"), context.capture());
        assertThat(context.getValue().templateType()).isEqualTo(BookEmailType.TRANSLATION_READY);
        assertThat(context.getValue().getValue(BookEmailComposerService.LANGUAGE_NAME))
                .isEqualTo("Ukrainian");
        assertThat(context.getValue().getValue(BookEmailComposerService.MONTH)).isEqualTo("August");
        assertThat(context.getValue().getValue(BookEmailComposerService.PATH))
                .isEqualTo("/reader/effi-briest-de-original?parallel=UK");
        assertThat(context.getValue().getValue(BookEmailComposerService.REQUESTS_LEFT))
                .isEqualTo("You have 2 requests left this month.");
    }

    @Test
    void theBooksSwitchSilencesEveryReadingMail() {
        User reader = user(false);

        service.translationReady(reader, "Effi Briest", Language.UK, "August", "/reader/x?parallel=UK", "");
        service.translationDeclined(reader, "Effi Briest", Language.UK);
        service.suggestionPublished(reader, "Der Schimmelreiter", "August", "/books/der-schimmelreiter");
        service.suggestionDeclined(reader, "Der Schimmelreiter");

        verify(composer, never()).sendEmail(any(), any(), any());
    }

    @Test
    void monthsAreNamedInEnglishFromTheUtcCalendar() {
        assertThat(BookEmailService.monthOf(Instant.parse("2026-08-31T23:30:00Z")))
                .isEqualTo("August");
        assertThat(BookEmailService.monthOf(Instant.parse("2026-09-01T00:00:00Z")))
                .isEqualTo("September");
    }

    private static User user(boolean bookEmails) {
        User user = User.builder()
                .id(UUID.randomUUID())
                .username("oleg")
                .email("oleg@example.com")
                .build();
        user.setProfile(
                Profile.builder().user(user).bookEmailNotifications(bookEmails).build());
        return user;
    }
}
