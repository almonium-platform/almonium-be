package com.almonium.auth.oauth2.repository;

import com.almonium.auth.common.enums.AuthProviderType;
import com.almonium.auth.oauth2.model.OAuth2Principal;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuth2PrincipalRepository extends JpaRepository<OAuth2Principal, Long> {
    Optional<OAuth2Principal> findByProviderAndProviderUserId(AuthProviderType provider, String providerUserId);
}
