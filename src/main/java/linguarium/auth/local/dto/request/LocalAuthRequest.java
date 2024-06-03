package linguarium.auth.local.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LocalAuthRequest(
        @NotBlank(message = "Email cannot be blank") @Email String email,
        @NotBlank(message = "Password cannot be blank")
                @Size(min = 8, message = "Password must be at least 8 characters long")
                String password) {}
