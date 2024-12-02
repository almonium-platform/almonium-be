package com.almonium.auth.local.service.impl;

import com.almonium.auth.local.service.TokenGenerator;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class ApacheAlphanumericGeneratorImpl implements TokenGenerator {

    @Override
    public String generateOTP(int length) {
        return new RandomStringGenerator.Builder().withinRange('0', 'z').get().generate(length);
    }
}
