package com.almonium.user.core.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.local.service.impl.SecureRandomNumericGeneratorImpl;
import com.almonium.user.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class UsernameGeneratorImpl implements UsernameGenerator {
    private static final int MAX_ATTEMPTS = 5;
    private static final String SANITIZING_REGEX = "[^a-zA-Z0-9_]";
    private static final int MAX_USERNAME_LENGTH = 10;
    UserRepository userRepository;
    SecureRandomNumericGeneratorImpl randomNumericGenerator;

    @Override
    public String generateUsername(String email) {
        String username = email.split("@")[0].replaceAll(SANITIZING_REGEX, "");

        int attempts = 0;
        while (userRepository.existsByUsername(username) && attempts < MAX_ATTEMPTS) {
            username += randomNumericGenerator.generateOTP(1);
            attempts++;
        }

        if (attempts == MAX_ATTEMPTS) {
            log.error("Could not generate a unique username for email: {} in {} attempts", username, MAX_ATTEMPTS);
            username = randomNumericGenerator.generateOTP(MAX_USERNAME_LENGTH);
        }

        log.debug("Generated username: {} for email: {}", username, email);
        return username;
    }
}
