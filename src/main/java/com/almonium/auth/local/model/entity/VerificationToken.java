package com.almonium.auth.local.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.local.model.enums.TokenType;
import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class VerificationToken {

    @Id
    @UuidV7
    UUID id;

    String token;

    @OneToOne(targetEntity = LocalPrincipal.class)
    @JoinColumn(name = "principal_id")
    LocalPrincipal principal;

    Instant expiresAt;

    @CreatedDate
    Instant createdAt;

    @Enumerated(EnumType.STRING)
    TokenType tokenType;

    public VerificationToken(LocalPrincipal principal, String token, TokenType tokenType, long minutes) {
        if (minutes <= 0) {
            throw new IllegalArgumentException("minutes must be greater than 0");
        }
        this.principal = principal;
        this.token = token;
        this.tokenType = tokenType;
        expiresAt = Instant.now().plusSeconds(minutes * 60);
    }
}
