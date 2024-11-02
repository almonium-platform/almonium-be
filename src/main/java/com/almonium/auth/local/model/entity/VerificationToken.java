package com.almonium.auth.local.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.local.model.enums.TokenType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String token;

    @OneToOne(targetEntity = LocalPrincipal.class)
    @JoinColumn(name = "principal_id")
    LocalPrincipal principal;

    LocalDateTime expiresAt;

    @CreatedDate
    LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    TokenType tokenType;

    public VerificationToken(LocalPrincipal principal, String token, TokenType tokenType, long minutes) {
        this.principal = principal;
        this.token = token;
        this.tokenType = tokenType;
        expiresAt = LocalDateTime.now().plusMinutes(minutes);
    }
}
