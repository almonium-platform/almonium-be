package com.almonium.auth.local.service;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class ApacheAlphanumericGeneratorImpl implements TokenGenerator {
    private static final RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder()
            .withinRange('0', '9')
            .withinRange('a', 'z')
            .withinRange('A', 'Z')
            .get();

    @Override
    public String generateOTP(int length) {
        return randomStringGenerator.generate(length);
    }
}
