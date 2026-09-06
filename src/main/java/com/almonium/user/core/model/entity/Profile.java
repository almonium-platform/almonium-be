package com.almonium.user.core.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
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
    UUID id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    User user;

    String avatarUrl;

    boolean hidden;

    /**
     * Whether the two connection emails (request received, request accepted) are sent. The in-app bell and push
     * notifications do not read this; a member who turns it off still sees the request in the product.
     */
    @Builder.Default
    boolean socialEmailNotifications = true;

    @CreatedDate
    LocalDateTime lastLogin;

    /**
     * When the account last swapped which language is active. The downgrade pick does not stamp this: choosing what
     * to keep is not the same as spending a switch.
     */
    Instant lastActiveSwitchAt;

    /** Which language the user asked to keep when the plan ends. Consumed and cleared the moment it is honoured. */
    @Enumerated(EnumType.STRING)
    Language downgradeKeepLanguage;

    @JdbcTypeCode(SqlTypes.JSON)
    Map<String, Object> uiPreferences;
}
