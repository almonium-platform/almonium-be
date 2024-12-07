package com.almonium.auth.local.service;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class ApacheAlphanumericGeneratorImpl implements TokenGenerator {
    private static final RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder()
            .withinRange('0', 'z')
            .filteredBy(Character::isLetterOrDigit)
            .get();

    @Override
    public String generateOTP(int length) {
        return randomStringGenerator.generate(length);
    }
}
