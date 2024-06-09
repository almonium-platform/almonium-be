package com.almonium.user.core.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.engine.translator.model.enums.Language;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class Profile {
    @Id
    Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    User user;

    String background;
    String avatarUrl;

    @Builder.Default
    int dailyGoal = 5;

    boolean friendshipRequestsBlocked;

    @CreatedDate
    LocalDateTime lastLogin;

    @Builder.Default
    int streak = 1;

    @Builder.Default
    Language uiLang = Language.EN;
}
