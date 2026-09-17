package com.almonium.learning.book.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.analyzer.translator.util.LanguageNames;
import com.almonium.infra.email.model.dto.EmailContext;
import com.almonium.infra.email.service.BookEmailComposerService;
import com.almonium.learning.book.model.enums.BookEmailType;
import com.almonium.user.core.model.entity.User;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Sends the reading mails one recipient at a time and never lets a mail failure undo the thing it announces:
 * a publication that already happened, a decision an operator already took. The Books switch in Settings
 * covers these mails and the matching pushes together; the bell row is not governed by it.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class BookEmailService {
    BookEmailComposerService composer;

    public void translationReady(
            User user, String bookTitle, Language language, String monthAsked, String actionPath, String requestsLeft) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put(BookEmailComposerService.BOOK_TITLE, bookTitle);
        attributes.put(BookEmailComposerService.LANGUAGE_NAME, LanguageNames.englishName(language));
        attributes.put(BookEmailComposerService.MONTH, monthAsked);
        attributes.put(BookEmailComposerService.PATH, actionPath);
        attributes.put(BookEmailComposerService.REQUESTS_LEFT, requestsLeft == null ? "" : requestsLeft);
        send(user, BookEmailType.TRANSLATION_READY, attributes);
    }

    public void translationDeclined(User user, String bookTitle, Language language) {
        send(
                user,
                BookEmailType.TRANSLATION_DECLINED,
                Map.of(
                        BookEmailComposerService.BOOK_TITLE,
                        bookTitle,
                        BookEmailComposerService.LANGUAGE_NAME,
                        LanguageNames.englishName(language)));
    }

    public void suggestionPublished(User user, String bookTitle, String monthSuggested, String actionPath) {
        send(
                user,
                BookEmailType.SUGGESTION_PUBLISHED,
                Map.of(
                        BookEmailComposerService.BOOK_TITLE,
                        bookTitle,
                        BookEmailComposerService.MONTH,
                        monthSuggested,
                        BookEmailComposerService.PATH,
                        actionPath));
    }

    public void requestPublished(User user, String bookTitle, String monthAsked, String actionPath) {
        send(
                user,
                BookEmailType.REQUEST_PUBLISHED,
                Map.of(
                        BookEmailComposerService.BOOK_TITLE,
                        bookTitle,
                        BookEmailComposerService.MONTH,
                        monthAsked,
                        BookEmailComposerService.PATH,
                        actionPath));
    }

    public void suggestionDeclined(User user, String bookTitle) {
        send(user, BookEmailType.SUGGESTION_DECLINED, Map.of(BookEmailComposerService.BOOK_TITLE, bookTitle));
    }

    /** "August": the month a request or suggestion was made, in the calendar the product keeps (UTC). */
    public static String monthOf(Instant instant) {
        Instant when = instant == null ? Instant.now() : instant;
        return when.atZone(ZoneOffset.UTC).getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }

    private void send(User user, BookEmailType type, Map<String, String> attributes) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            return;
        }
        if (user.getProfile() != null && !user.getProfile().isBookEmailNotifications()) {
            log.info("Skipping {} mail for user {}: book notifications are off", type, user.getId());
            return;
        }
        try {
            composer.sendEmail(user.getUsername(), user.getEmail(), new EmailContext<>(type, attributes));
        } catch (Exception exception) {
            log.warn("Could not send {} mail to user {}", type, user.getId(), exception);
        }
    }
}
