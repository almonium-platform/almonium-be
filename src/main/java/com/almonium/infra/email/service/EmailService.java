package com.almonium.infra.email.service;

import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.codec.CharEncoding.UTF_8;

import com.almonium.infra.email.dto.EmailDto;
import com.almonium.infra.email.exception.EmailConfigurationException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class EmailService {
    private static final String ALMONIUM_FROM_FORMAT = "Almonium <%s>";
    final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    String from;

    public void sendEmail(EmailDto emailDto) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, UTF_8);
            mimeMessageHelper.setSubject(emailDto.subject());
            mimeMessageHelper.setTo(emailDto.recipient());
            mimeMessageHelper.setFrom(String.format(ALMONIUM_FROM_FORMAT, from));
            mimeMessageHelper.setText(emailDto.body(), true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Failed to send email: {}", e.getMessage());
            throw new EmailConfigurationException("Failed to send email: " + e.getMessage(), e);
        }
    }
}
