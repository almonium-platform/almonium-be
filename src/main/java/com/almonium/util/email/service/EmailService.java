package com.almonium.util.email.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.util.email.dto.EmailDto;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class EmailService {
    JavaMailSender mailSender;

    public void sendEmail(EmailDto emailDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailDto.recipient());
        message.setSubject(emailDto.subject());
        message.setText(emailDto.body());
        mailSender.send(message);
    }
}
