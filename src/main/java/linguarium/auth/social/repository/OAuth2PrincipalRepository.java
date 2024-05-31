package linguarium.auth.social.repository;

import java.util.Optional;
import linguarium.auth.common.enums.AuthProviderType;
import linguarium.auth.social.model.OAuth2Principal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuth2PrincipalRepository extends JpaRepository<OAuth2Principal, Long> {
    Optional<OAuth2Principal> findByProviderAndProviderUserId(AuthProviderType provider, String providerUserId);
}
