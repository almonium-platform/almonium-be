package com.almonium.infra.email.service;

import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.codec.CharEncoding.UTF_8;

import com.almonium.infra.email.dto.EmailDto;
import com.almonium.infra.email.exception.EmailConfigurationException;
import com.almonium.util.HtmlFileWriter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class EmailService {
    private static final String ALMONIUM_FROM_FORMAT = "Almonium <%s>";
    private static final String STATIC_LOGO_IMG = "static/logo-white-2.png";
    private static final String STATIC_TITLE_IMG = "static/title-white.png";
    private static final Map<String, String> EMAIL_ASSETS = Map.of(
            "logoHeader", STATIC_LOGO_IMG,
            "titleHeader", STATIC_TITLE_IMG);

    final JavaMailSender mailSender;
    final HtmlFileWriter htmlFileWriter;

    @Value("${spring.mail.username}")
    String from;

    @Value("${app.email.dry-run}")
    boolean isEmailSendingSimulated;

    public void sendEmail(EmailDto emailDto) {
        try {
            MimeMessage mimeMessage = createMimeMessage(emailDto);
            if (isEmailSendingSimulated) {
                log.info("Email sending is disabled. Skipping sending email to {}", emailDto.recipient());
                htmlFileWriter.saveMimeMessageToFile(mimeMessage);
            } else {
                mailSender.send(mimeMessage);
            }
        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", emailDto.recipient(), e.getMessage());
            throw new EmailConfigurationException("Failed to send email: " + e.getMessage(), e);
        }
    }

    private MimeMessage createMimeMessage(EmailDto emailDto) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, UTF_8);

        mimeMessageHelper.setSubject(emailDto.subject());
        mimeMessageHelper.setTo(emailDto.recipient());
        mimeMessageHelper.setFrom(String.format(ALMONIUM_FROM_FORMAT, from));
        mimeMessageHelper.setText(emailDto.body(), true);

        addAssetsToEmail(mimeMessageHelper);
        return mimeMessage;
    }

    private void addAssetsToEmail(MimeMessageHelper mimeMessageHelper) throws MessagingException {
        for (Map.Entry<String, String> entry : EMAIL_ASSETS.entrySet()) {
            ClassPathResource asset = new ClassPathResource(entry.getValue());
            mimeMessageHelper.addInline(entry.getKey(), asset);
        }
    }
}
