package linguarium.auth.local.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import linguarium.auth.local.model.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

    void deleteByExpiryDateBefore(LocalDateTime now);
}
