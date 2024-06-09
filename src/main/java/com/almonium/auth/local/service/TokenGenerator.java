package com.almonium.auth.local.service;

public interface TokenGenerator {
    String generateOTP(int length);
}
