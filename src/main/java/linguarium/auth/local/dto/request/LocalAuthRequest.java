package linguarium.auth.local.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LocalAuthRequest(@NotBlank String email, @NotBlank String password) {
}
