package com.almonium.auth.oauth2.other.repository;

import com.almonium.auth.common.model.enums.AuthProviderType;
import com.almonium.auth.oauth2.other.model.entity.OAuth2Principal;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuth2PrincipalRepository extends JpaRepository<OAuth2Principal, Long> {
    Optional<OAuth2Principal> findByProviderAndProviderUserId(AuthProviderType provider, String providerUserId);
}
