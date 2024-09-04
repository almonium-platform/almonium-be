package com.almonium.auth.token.model.entity;

import com.almonium.user.core.model.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RefreshToken {
    @Id
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Instant issueDate;

    private Instant expiryDate;

    private boolean revoked;

    public RefreshToken(UUID id, User user, Instant issueDate, Instant expiryDate) {
        this.id = id;
        this.user = user;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.revoked = false;
    }
}
