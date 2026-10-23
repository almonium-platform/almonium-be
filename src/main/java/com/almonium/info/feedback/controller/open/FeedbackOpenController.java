package com.almonium.info.feedback.controller.open;

import com.almonium.info.feedback.dto.FeedbackRequest;
import com.almonium.info.feedback.service.FeedbackRateLimiter;
import com.almonium.info.feedback.service.FeedbackService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/** The landing page's question to the founder. Open to guests: no account and no email are needed to answer it. */
@RestController
@RequestMapping("/public/feedback")
@RequiredArgsConstructor
public class FeedbackOpenController {
    private final FeedbackService feedbackService;
    private final FeedbackRateLimiter rateLimiter;

    @PostMapping
    public ResponseEntity<Void> submit(@Valid @RequestBody FeedbackRequest request, HttpServletRequest httpRequest) {
        if (!rateLimiter.tryAcquire(httpRequest.getRemoteAddr())) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many messages; try again later.");
        }
        feedbackService.submit(request);
        return ResponseEntity.accepted().build();
    }
}
