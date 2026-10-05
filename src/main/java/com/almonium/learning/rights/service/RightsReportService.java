package com.almonium.learning.rights.service;

import com.almonium.auth.common.model.PrincipalDetails;
import com.almonium.infra.email.dto.EmailDto;
import com.almonium.infra.email.service.EmailService;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.repository.BookRepository;
import com.almonium.learning.rights.dto.request.RightsReportRequest;
import com.almonium.learning.rights.dto.response.RightsReportReceipt;
import com.almonium.learning.rights.model.entity.RightsReport;
import com.almonium.learning.rights.model.enums.RightsReportStatus;
import com.almonium.learning.rights.repository.RightsReportRepository;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Locale;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

/**
 * The report button: anyone, signed in or not, can say a book should not be here or not in their country. The report
 * lands in the /ops rights queue and a copy goes to the copyright mailbox at once, so a takedown can follow within
 * minutes. Nothing is hidden automatically: a report is a claim, not a verdict.
 */
@Slf4j
@Service
@Transactional
public class RightsReportService {
    private final RightsReportRepository reportRepository;
    private final BookRepository bookRepository;
    private final ReaderLocation readerLocation;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final String copyrightAddress;

    public RightsReportService(
            RightsReportRepository reportRepository,
            BookRepository bookRepository,
            ReaderLocation readerLocation,
            EmailService emailService,
            UserRepository userRepository,
            @Value("${app.rights.copyright-address:copyright@almonium.com}") String copyrightAddress) {
        this.reportRepository = reportRepository;
        this.bookRepository = bookRepository;
        this.readerLocation = readerLocation;
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.copyrightAddress = copyrightAddress;
    }

    /** A report from whoever is calling: the signed-in member when there is a session, a guest otherwise. */
    public RightsReportReceipt report(String editionSlug, RightsReportRequest request) {
        return report(editionSlug, request, currentMember().orElse(null));
    }

    public RightsReportReceipt report(String editionSlug, RightsReportRequest request, User member) {
        Book book = bookRepository
                .findAnyByEditionSlug(editionSlug)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        String email = member != null ? member.getEmail() : blankToNull(request.email());
        if (email == null) {
            throw new BadUserRequestActionException("Leave an email address so we can answer you.");
        }
        RightsReport report = new RightsReport();
        report.setBook(book);
        report.setWorkSlug(book.getWorkSlug());
        report.setReporter(member);
        report.setReporterEmail(email);
        report.setReporterName(blankToNull(request.name()));
        report.setReporterRole(request.role());
        report.setCountry(
                blankToNull(request.country()) == null
                        ? null
                        : request.country().toUpperCase(Locale.ROOT));
        report.setDetectedCountry(readerLocation.detectedCountry().orElse(null));
        report.setMessage(request.message().trim());
        report.setStatus(RightsReportStatus.OPEN);
        RightsReport saved = reportRepository.save(report);
        notifyMailbox(saved, book);
        return new RightsReportReceipt(saved.getId(), copyrightAddress);
    }

    private void notifyMailbox(RightsReport report, Book book) {
        String text = String.join(
                "\n",
                "A " + report.getReporterRole().name().toLowerCase(Locale.ROOT).replace('_', ' ') + " reported \""
                        + book.getTitle() + "\" by " + book.getAuthor() + " (" + book.getEditionSlug() + ").",
                "About: " + (report.getCountry() == null ? "everywhere" : report.getCountry()) + ". Sent from: "
                        + (report.getDetectedCountry() == null ? "unknown" : report.getDetectedCountry()) + ".",
                "From: " + (report.getReporterName() == null ? "" : report.getReporterName() + " ") + "<"
                        + report.getReporterEmail() + ">",
                "",
                report.getMessage(),
                "",
                "Act on it in /ops → Rights (report " + report.getId() + ").");
        try {
            emailService.sendEmail(new EmailDto(
                    copyrightAddress,
                    "Rights report: " + book.getTitle(),
                    "<pre style=\"font-family:inherit;white-space:pre-wrap\">" + HtmlUtils.htmlEscape(text) + "</pre>",
                    text));
        } catch (RuntimeException exception) {
            // The report is saved and in the /ops queue; a failed copy to the mailbox must not lose it.
            log.warn("Rights report {} saved but not mailed: {}", report.getId(), exception.getMessage());
        }
    }

    private Optional<User> currentMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof PrincipalDetails principal) {
            return userRepository.findById(principal.getUserId());
        }
        return Optional.empty();
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
