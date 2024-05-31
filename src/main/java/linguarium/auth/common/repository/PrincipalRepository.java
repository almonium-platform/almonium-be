package linguarium.auth.common.repository;

import java.util.Optional;
import linguarium.auth.common.entity.Principal;
import linguarium.auth.common.enums.AuthProviderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrincipalRepository extends JpaRepository<Principal, Long> {
    Optional<Principal> findByProviderAndProviderUserId(AuthProviderType provider, String providerUserId);
}
