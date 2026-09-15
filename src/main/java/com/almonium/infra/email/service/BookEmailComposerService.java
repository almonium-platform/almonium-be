package com.almonium.infra.email.service;

import com.almonium.config.properties.AppProperties;
import com.almonium.infra.email.model.dto.EmailContext;
import com.almonium.infra.email.model.dto.EmailSubjectTemplate;
import com.almonium.learning.book.model.enums.BookEmailType;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

/**
 * The reading mails (G8a). Each says one thing and, where there is a book to open, links it with one button.
 * None shows a date estimate or a queue position: there are none to show. They are transactional, so the
 * footer carries Settings and no Unsubscribe.
 */
@Service
public class BookEmailComposerService extends EmailComposerService<BookEmailType> {
    public static final String BOOK_TITLE = "bookTitle";
    public static final String LANGUAGE_NAME = "languageName";
    /** "August": when the reader asked or suggested. */
    public static final String MONTH = "month";
    /** The path under the web domain the button opens; blank for the plain mails. */
    public static final String PATH = "path";
    /** The closing line of the fulfilment mail, already worded; blank when the plan is unlimited. */
    public static final String REQUESTS_LEFT = "requestsLeft";

    private static final String BUTTON_URL_PLACEHOLDER = "url";
    private static final String SUBFOLDER = "books";

    private static final Map<BookEmailType, EmailSubjectTemplate> TYPE_EMAIL_SUBJECT_TEMPLATE_MAP = Map.of(
            BookEmailType.TRANSLATION_READY,
            new EmailSubjectTemplate(
                    "%s now reads alongside %s", "The book you asked for in %s is ready.", "translation-ready"),
            BookEmailType.TRANSLATION_DECLINED,
            new EmailSubjectTemplate(
                    "About your request for %s",
                    "We could not add %s this time; your request is free again.", "translation-declined"),
            BookEmailType.SUGGESTION_PUBLISHED,
            new EmailSubjectTemplate(
                    "%s is now in the library", "The book you suggested in %s is ready.", "suggestion-published"),
            BookEmailType.SUGGESTION_DECLINED,
            new EmailSubjectTemplate(
                    "About your suggestion for %s",
                    "We could not add it to the library; your private copy is unchanged.", "suggestion-declined"));

    public BookEmailComposerService(
            EmailService emailService, SpringTemplateEngine templateEngine, AppProperties appProperties) {
        super(emailService, templateEngine, appProperties);
    }

    @Override
    public Map<BookEmailType, EmailSubjectTemplate> getTemplateTypeConfigMap() {
        return TYPE_EMAIL_SUBJECT_TEMPLATE_MAP;
    }

    @Override
    public Map<String, String> getCustomPlaceholders(EmailContext<BookEmailType> emailContext) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put(BOOK_TITLE, valueOr(emailContext, BOOK_TITLE, ""));
        placeholders.put(LANGUAGE_NAME, valueOr(emailContext, LANGUAGE_NAME, ""));
        placeholders.put(MONTH, valueOr(emailContext, MONTH, ""));
        placeholders.put(REQUESTS_LEFT, valueOr(emailContext, REQUESTS_LEFT, ""));
        String path = valueOr(emailContext, PATH, "");
        placeholders.put(BUTTON_URL_PLACEHOLDER, buildActionUrl(path.isBlank() ? "/read" : path));
        return placeholders;
    }

    @Override
    protected String buildSubject(EmailSubjectTemplate template, EmailContext<BookEmailType> emailContext) {
        return switch (emailContext.templateType()) {
            case TRANSLATION_READY ->
                String.format(
                        template.subject(), emailContext.getValue(BOOK_TITLE), emailContext.getValue(LANGUAGE_NAME));
            case TRANSLATION_DECLINED, SUGGESTION_PUBLISHED, SUGGESTION_DECLINED ->
                String.format(template.subject(), emailContext.getValue(BOOK_TITLE));
        };
    }

    @Override
    protected String buildPreheader(EmailSubjectTemplate template, EmailContext<BookEmailType> emailContext) {
        return switch (emailContext.templateType()) {
            case TRANSLATION_READY, SUGGESTION_PUBLISHED ->
                String.format(template.preheader(), valueOr(emailContext, MONTH, "the month you asked"));
            case TRANSLATION_DECLINED -> String.format(template.preheader(), emailContext.getValue(LANGUAGE_NAME));
            case SUGGESTION_DECLINED -> template.preheader();
        };
    }

    @Override
    public String getSubfolder() {
        return SUBFOLDER;
    }

    private static String valueOr(EmailContext<BookEmailType> context, String key, String fallback) {
        String value = context.getValue(key);
        return value == null ? fallback : value;
    }
}
