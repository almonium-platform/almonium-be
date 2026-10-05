package com.almonium.learning.rights.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.infra.email.dto.EmailDto;
import com.almonium.infra.email.service.EmailService;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.repository.BookRepository;
import com.almonium.learning.rights.dto.request.RightsReportRequest;
import com.almonium.learning.rights.model.entity.RightsReport;
import com.almonium.learning.rights.model.enums.ReporterRole;
import com.almonium.learning.rights.model.enums.RightsReportStatus;
import com.almonium.learning.rights.repository.RightsReportRepository;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class RightsReportServiceTest {
    RightsReportRepository reports = mock(RightsReportRepository.class);
    BookRepository books = mock(BookRepository.class);
    ReaderLocation location = mock(ReaderLocation.class);
    EmailService email = mock(EmailService.class);
    RightsReportService service = new RightsReportService(
            reports, books, location, email, mock(UserRepository.class), "copyright@almonium.com");

    @BeforeEach
    void aBook() {
        Book book = new Book();
        book.setEditionSlug("the-little-prince-en");
        book.setWorkSlug("the-little-prince");
        book.setTitle("The Little Prince");
        book.setAuthor("Antoine de Saint-Exupéry");
        when(books.findAnyByEditionSlug("the-little-prince-en")).thenReturn(Optional.of(book));
        when(location.detectedCountry()).thenReturn(Optional.of("FR"));
        when(reports.save(any(RightsReport.class))).thenAnswer(call -> {
            RightsReport report = call.getArgument(0);
            report.setId(UUID.randomUUID());
            return report;
        });
    }

    @Test
    void aGuestRightsHolderReportsWithAnEmailAndTheMailboxHearsAtOnce() {
        var receipt = service.report(
                "the-little-prince-en",
                new RightsReportRequest(
                        ReporterRole.RIGHTS_HOLDER,
                        "Gallimard",
                        "rights@example.fr",
                        "fr",
                        "Le Petit Prince is still protected in France."),
                null);

        ArgumentCaptor<RightsReport> saved = ArgumentCaptor.forClass(RightsReport.class);
        verify(reports).save(saved.capture());
        assertThat(saved.getValue().getCountry()).isEqualTo("FR");
        assertThat(saved.getValue().getDetectedCountry()).isEqualTo("FR");
        assertThat(saved.getValue().getStatus()).isEqualTo(RightsReportStatus.OPEN);
        assertThat(saved.getValue().getWorkSlug()).isEqualTo("the-little-prince");
        ArgumentCaptor<EmailDto> mail = ArgumentCaptor.forClass(EmailDto.class);
        verify(email).sendEmail(mail.capture());
        assertThat(mail.getValue().recipient()).isEqualTo("copyright@almonium.com");
        assertThat(mail.getValue().textBody()).contains("Gallimard").contains("About: FR");
        assertThat(receipt.contact()).isEqualTo("copyright@almonium.com");
    }

    @Test
    void withoutASessionTheReporterIsAGuest() {
        service.report(
                "the-little-prince-en",
                new RightsReportRequest(
                        ReporterRole.OTHER, null, "someone@example.com", null, "A guest reporting this."));

        ArgumentCaptor<RightsReport> saved = ArgumentCaptor.forClass(RightsReport.class);
        verify(reports).save(saved.capture());
        assertThat(saved.getValue().getReporter()).isNull();
        assertThat(saved.getValue().getReporterEmail()).isEqualTo("someone@example.com");
    }

    @Test
    void aGuestWithoutAnEmailIsAskedForOne() {
        assertThatThrownBy(() -> service.report(
                        "the-little-prince-en",
                        new RightsReportRequest(ReporterRole.READER, null, " ", null, "This should not be here."),
                        null))
                .isInstanceOf(BadUserRequestActionException.class);
        verify(reports, never()).save(any());
    }

    @Test
    void aMemberIsKnownByTheirAccountAndAFailedMailDoesNotLoseTheReport() {
        User member = new User();
        member.setEmail("reader@example.com");
        doThrow(new IllegalStateException("mail down")).when(email).sendEmail(any());

        service.report(
                "the-little-prince-en",
                new RightsReportRequest(ReporterRole.READER, null, null, "", "Not public domain where I live."),
                member);

        ArgumentCaptor<RightsReport> saved = ArgumentCaptor.forClass(RightsReport.class);
        verify(reports).save(saved.capture());
        assertThat(saved.getValue().getReporterEmail()).isEqualTo("reader@example.com");
        assertThat(saved.getValue().getCountry()).isNull();
    }
}
