package com.almonium.user.core.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.subscription.constant.AppLimits;
import com.almonium.user.core.repository.UserRepository;
import java.security.SecureRandom;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UsernameGeneratorImpl implements UsernameGenerator {
    private static final int MAX_ATTEMPTS = 5;
    private static final String SANITIZING_REGEX = "[^a-zA-Z0-9_]";

    UserRepository userRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public String generateUsername(String email) {
        String username = email.split("@")[0].replaceAll(SANITIZING_REGEX, "").toLowerCase();

        int attempts = 0;
        while (userRepository.existsByUsername(username) && attempts < MAX_ATTEMPTS) {
            username += secureRandom.nextInt(10);
            attempts++;
        }

        if (attempts == MAX_ATTEMPTS) {
            log.error("Could not generate a unique username for email: {} in {} attempts", username, MAX_ATTEMPTS);
            username = secureRandom
                    .ints(AppLimits.MAX_USERNAME_LENGTH, 0, 10)
                    .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                    .toString();
        }

        log.debug("Generated username: {} for email: {}", username, email);
        return username;
    }
}
