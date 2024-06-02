package linguarium.auth.local.service;

public interface TokenGenerator {
    String generateOTP(int length);
}
