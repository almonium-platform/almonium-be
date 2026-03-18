package com.almonium.infra.email.service;

import static lombok.AccessLevel.PRIVATE;
import static org.mockito.Mockito.verify;

import com.almonium.infra.email.dto.EmailDto;
import com.almonium.util.HtmlFileWriter;
import com.almonium.util.config.AppConfigPropertiesTest;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = PRIVATE)
public class EmailServiceTest extends AppConfigPropertiesTest {
    EmailService emailService;

    @Mock
    HtmlFileWriter htmlFileWriter;

    @BeforeEach
    void setUp() {
        emailService = new EmailService(htmlFileWriter, appProperties);
    }

    @DisplayName("Should save email to file when email sending is disabled")
    @Test
    void givenEmailSendingDisabled_whenSendEmail_thenSaveToFile() {
        // Arrange
        EmailDto emailDto = new EmailDto("test@example.com", "Test Subject", "Test Body");
        // it's true in test properties, but added for more resilience and readability
        appProperties.getEmail().setDryRun(true);

        // Act
        emailService.sendEmail(emailDto);

        // Assert
        verify(htmlFileWriter).saveEmailToFile(emailDto);
    }
}
