package linguarium.auth.oauth2.repository;

import java.util.Optional;
import linguarium.auth.oauth2.model.entity.ProviderAccount;
import linguarium.auth.oauth2.model.enums.AuthProviderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderAccountRepository extends JpaRepository<ProviderAccount, Long> {
    Optional<ProviderAccount> findByProviderAndProviderUserId(AuthProviderType provider, String providerUserId);
}
