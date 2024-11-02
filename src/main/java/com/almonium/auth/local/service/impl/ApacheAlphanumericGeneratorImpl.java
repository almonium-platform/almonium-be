package com.almonium.auth.local.service.impl;

import com.almonium.auth.local.service.TokenGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class ApacheAlphanumericGeneratorImpl implements TokenGenerator {

    @Override
    public String generateOTP(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }
}
