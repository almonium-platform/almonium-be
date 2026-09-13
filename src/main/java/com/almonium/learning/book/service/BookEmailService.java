package com.almonium.learning.book.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.infra.email.model.dto.EmailContext;
import com.almonium.infra.email.service.BookEmailComposerService;
import com.almonium.learning.book.model.enums.BookEmailType;
import com.almonium.user.core.model.entity.User;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Sends the reading mails one recipient at a time and never lets a mail failure undo the thing it announces:
 * a publication that already happened, a decision an operator already took.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class BookEmailService {
    BookEmailComposerService composer;

    public void translationReady(Collection<User> users, String bookTitle, Language language, String editionSlug) {
        send(
                users,
                BookEmailType.TRANSLATION_READY,
                Map.of(
                        BookEmailComposerService.BOOK_TITLE,
                        bookTitle,
                        BookEmailComposerService.LANGUAGE_NAME,
                        languageName(language),
                        BookEmailComposerService.PATH,
                        "/books/" + editionSlug));
    }

    public void translationDeclined(Collection<User> users, String bookTitle, Language language) {
        send(
                users,
                BookEmailType.TRANSLATION_DECLINED,
                Map.of(
                        BookEmailComposerService.BOOK_TITLE,
                        bookTitle,
                        BookEmailComposerService.LANGUAGE_NAME,
                        languageName(language)));
    }

    public void suggestionPublished(Collection<User> users, String bookTitle, String editionSlug) {
        send(
                users,
                BookEmailType.SUGGESTION_PUBLISHED,
                Map.of(
                        BookEmailComposerService.BOOK_TITLE,
                        bookTitle,
                        BookEmailComposerService.PATH,
                        "/books/" + editionSlug));
    }

    public void suggestionDeclined(Collection<User> users, String bookTitle) {
        send(users, BookEmailType.SUGGESTION_DECLINED, Map.of(BookEmailComposerService.BOOK_TITLE, bookTitle));
    }

    private void send(Collection<User> users, BookEmailType type, Map<String, String> attributes) {
        for (User user : users) {
            if (user.getEmail() == null || user.getEmail().isBlank()) {
                continue;
            }
            try {
                composer.sendEmail(user.getUsername(), user.getEmail(), new EmailContext<>(type, attributes));
            } catch (Exception exception) {
                log.warn("Could not send {} mail to user {}", type, user.getId(), exception);
            }
        }
    }

    /** "Ukrainian", not "UK": the code is ours, the name is the reader's. */
    static String languageName(Language language) {
        String code = language.name().toLowerCase(Locale.ROOT);
        String name = Locale.forLanguageTag(code).getDisplayLanguage(Locale.ENGLISH);
        return name == null || name.isBlank() || name.equalsIgnoreCase(code) ? language.name() : name;
    }
}
