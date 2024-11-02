package com.almonium.auth.token.model.entity;

import static lombok.AccessLevel.PRIVATE;

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
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class RefreshToken {
    @Id
    UUID id;

    @OneToOne
    @JoinColumn(name = "user_id")
    User user;

    Instant issueDate;

    Instant expiryDate;

    boolean revoked;

    public RefreshToken(UUID id, User user, Instant issueDate, Instant expiryDate) {
        this.id = id;
        this.user = user;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.revoked = false;
    }
}
