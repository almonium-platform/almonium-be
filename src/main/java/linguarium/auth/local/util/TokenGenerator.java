package linguarium.auth.local.util;

import java.security.SecureRandom;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TokenGenerator {
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateOTP(int length) {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(secureRandom.nextInt(10));
        }
        return code.toString();
    }
}
