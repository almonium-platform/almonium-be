package linguarium.auth.local.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne(targetEntity = LocalPrincipal.class)
    @JoinColumn(name = "principal_id")
    private LocalPrincipal principal;

    private LocalDateTime expiryDate;

    public VerificationToken(LocalPrincipal principal, String token, long minutes) {
        this.principal = principal;
        this.token = token;
        expiryDate = LocalDateTime.now().plusMinutes(minutes);
    }
}
