package linguarium.auth.local.repository;

import java.util.Optional;
import linguarium.auth.local.model.entity.LocalPrincipal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalPrincipalRepository extends JpaRepository<LocalPrincipal, Long> {
    Optional<LocalPrincipal> findByEmail(String email);
}
