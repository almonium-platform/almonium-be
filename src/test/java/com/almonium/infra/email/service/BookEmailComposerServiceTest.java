package com.almonium.infra.email.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.almonium.config.properties.AppProperties;
import com.almonium.infra.email.model.dto.EmailContext;
import com.almonium.learning.book.model.enums.BookEmailType;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.thymeleaf.spring6.SpringTemplateEngine;

class BookEmailComposerServiceTest {
    private final BookEmailComposerService composer = composer();

    @Test
    void theFulfilmentRowNamesTheBookTheLanguageAndTheMonth() {
        EmailContext<BookEmailType> context = new EmailContext<>(
                BookEmailType.TRANSLATION_READY,
                Map.of(
                        BookEmailComposerService.BOOK_TITLE, "Effi Briest",
                        BookEmailComposerService.LANGUAGE_NAME, "Ukrainian",
                        BookEmailComposerService.MONTH, "August",
                        BookEmailComposerService.PATH, "/reader/effi-briest-de-original?parallel=UK"));

        assertThat(subject(context)).isEqualTo("Effi Briest now reads alongside Ukrainian");
        assertThat(preheader(context)).isEqualTo("The book you asked for in August is ready.");
        assertThat(composer.getCustomPlaceholders(context))
                .containsEntry("url", "https://almonium.example/reader/effi-briest-de-original?parallel=UK")
                .containsEntry(BookEmailComposerService.REQUESTS_LEFT, "");
    }

    @Test
    void theDeclineRowNamesTheBookInTheSubjectAndTheLanguageInThePreheader() {
        EmailContext<BookEmailType> context = new EmailContext<>(
                BookEmailType.TRANSLATION_DECLINED,
                Map.of(
                        BookEmailComposerService.BOOK_TITLE, "Effi Briest",
                        BookEmailComposerService.LANGUAGE_NAME, "Ukrainian"));

        assertThat(subject(context)).isEqualTo("About your request for Effi Briest");
        assertThat(preheader(context)).isEqualTo("We could not add Ukrainian this time; your request is free again.");
        assertThat(composer.getCustomPlaceholders(context)).containsEntry("url", "https://almonium.example/read");
    }

    @Test
    void theSuggestionRowsUseTheWordSuggested() {
        EmailContext<BookEmailType> published = new EmailContext<>(
                BookEmailType.SUGGESTION_PUBLISHED,
                Map.of(
                        BookEmailComposerService.BOOK_TITLE, "Der Schimmelreiter",
                        BookEmailComposerService.MONTH, "August",
                        BookEmailComposerService.PATH, "/books/der-schimmelreiter-de-original"));
        EmailContext<BookEmailType> declined = new EmailContext<>(
                BookEmailType.SUGGESTION_DECLINED, Map.of(BookEmailComposerService.BOOK_TITLE, "Der Schimmelreiter"));

        assertThat(subject(published)).isEqualTo("Der Schimmelreiter is now in the library");
        assertThat(preheader(published)).isEqualTo("The book you suggested in August is ready.");
        assertThat(subject(declined)).isEqualTo("About your suggestion for Der Schimmelreiter");
        assertThat(preheader(declined))
                .isEqualTo("We could not add it to the library; your private copy is unchanged.");
    }

    private String subject(EmailContext<BookEmailType> context) {
        return composer.buildSubject(composer.getTemplateTypeConfigMap().get(context.templateType()), context);
    }

    private String preheader(EmailContext<BookEmailType> context) {
        return composer.buildPreheader(composer.getTemplateTypeConfigMap().get(context.templateType()), context);
    }

    private static BookEmailComposerService composer() {
        AppProperties properties = new AppProperties();
        properties.setWebDomain("https://almonium.example");
        return new BookEmailComposerService(mock(EmailService.class), mock(SpringTemplateEngine.class), properties);
    }
}
