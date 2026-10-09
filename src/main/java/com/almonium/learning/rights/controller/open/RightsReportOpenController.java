package com.almonium.learning.rights.controller.open;

import com.almonium.learning.rights.dto.request.RightsReportRequest;
import com.almonium.learning.rights.dto.response.RightsReportReceipt;
import com.almonium.learning.rights.service.RightsReportRateLimiter;
import com.almonium.learning.rights.service.RightsReportService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * The report button on every book. Open to guests, because the people with the best reason to report a book (its
 * rights holders) are rarely members; a signed-in reader is recognised by their session and needs no email.
 */
@RestController
@RequestMapping("/public/books/{editionSlug}/rights-reports")
@RequiredArgsConstructor
public class RightsReportOpenController {
    private final RightsReportService rightsReportService;
    private final RightsReportRateLimiter rateLimiter;

    @PostMapping
    public ResponseEntity<RightsReportReceipt> report(
            @PathVariable String editionSlug,
            @Valid @RequestBody RightsReportRequest request,
            HttpServletRequest httpRequest) {
        if (!rateLimiter.tryAcquire(httpRequest.getRemoteAddr())) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many reports; try again later.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(rightsReportService.report(editionSlug, request));
    }
}
