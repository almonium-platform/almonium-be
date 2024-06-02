package linguarium.util.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendVerificationEmail(String recipientEmail, String token) {
        String verificationUrl = "http://localhost:8080/auth/public/verify-email?token=" + token;
        String message = "Please verify your email by clicking the following link: " + verificationUrl;
        System.out.println(message);
    }
}
