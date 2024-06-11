package com.almonium.util.email.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.util.email.dto.EmailDto;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class EmailService {
    final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    String from;

    public void sendEmail(EmailDto emailDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailDto.recipient());
        message.setFrom(from);
        message.setSubject(emailDto.subject());
        message.setText(emailDto.body());
        mailSender.send(message);
    }
}
