package linguarium.auth.core.repository;

import java.util.Optional;
import linguarium.auth.core.entity.Principal;
import linguarium.auth.core.enums.AuthProviderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrincipalRepository extends JpaRepository<Principal, Long> {
    Optional<Principal> findByProviderAndProviderUserId(AuthProviderType provider, String providerUserId);
}
