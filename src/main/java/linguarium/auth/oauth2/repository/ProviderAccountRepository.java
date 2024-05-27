package linguarium.auth.oauth2.repository;

import linguarium.auth.oauth2.model.entity.ProviderAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderAccountRepository extends JpaRepository<ProviderAccount, Long> {
}
