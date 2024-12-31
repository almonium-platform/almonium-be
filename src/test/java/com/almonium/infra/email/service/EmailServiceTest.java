package com.almonium.infra.email.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.infra.email.dto.EmailDto;
import com.almonium.util.HtmlFileWriter;
import com.almonium.util.config.AppConfigPropertiesTest;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest extends AppConfigPropertiesTest {
    @Mock
    private JavaMailSender mailSender;

    @Mock
    private HtmlFileWriter htmlFileWriter;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        emailService = new EmailService(mailSender, htmlFileWriter, appProperties);
    }

    @DisplayName("Should send an email with the provided EmailDto")
    @Test
    void givenEmailDto_whenSendEmail_thenEmailIsSent() {
        // Arrange
        EmailDto emailDto = new EmailDto("test@example.com", "Test Subject", "Test Body");
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        appProperties.getEmail().setDryRun(false);

        // Act
        emailService.sendEmail(emailDto);

        // Assert
        verify(mailSender).send(any(MimeMessage.class));
    }

    @DisplayName("Should save email to file when email sending is disabled")
    @Test
    void givenEmailSendingDisabled_whenSendEmail_thenSaveToFile() {
        // Arrange
        EmailDto emailDto = new EmailDto("test@example.com", "Test Subject", "Test Body");
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        // it's true in test properties, but added for more resilience and readability
        appProperties.getEmail().setDryRun(true);

        // Act
        emailService.sendEmail(emailDto);

        // Assert
        verify(htmlFileWriter).saveMimeMessageToFile(mimeMessage);
        verify(mailSender, never()).send(any(MimeMessage.class));
    }
}
