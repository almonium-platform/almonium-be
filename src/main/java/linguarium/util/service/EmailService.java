package linguarium.util.service;

import static lombok.AccessLevel.PRIVATE;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class EmailService {
    private static final String SUBJECT = "Verify your email address";
    private static final String VERIFICATION_ENDPOINT = "/verify-email";
    private static final String BODY = "Please verify your email by clicking the following link: ";
    JavaMailSender mailSender;

    @NonFinal
    @Value("${app.server.domain}")
    String domain;

    public void sendVerificationEmail(String recipientEmail, String token) {
        String verificationUrl = domain + VERIFICATION_ENDPOINT + "?token=" + token;
        String message = BODY + verificationUrl;
        sendEmail(recipientEmail, message);
    }

    private void sendEmail(String to, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(EmailService.SUBJECT);
        message.setText(text);
        mailSender.send(message);
    }
}
