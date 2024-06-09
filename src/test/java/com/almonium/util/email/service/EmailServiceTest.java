package com.almonium.util.email.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.almonium.util.email.dto.EmailDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {
    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @DisplayName("Should send an email with the provided EmailDto")
    @Test
    void givenEmailDto_whenSendEmail_thenEmailIsSent() {
        // Arrange
        EmailDto emailDto = new EmailDto("test@example.com", "Test Subject", "Test Body");
        // Act
        emailService.sendEmail(emailDto);

        // Assert
        verify(mailSender).send(any(SimpleMailMessage.class));
    }
}
