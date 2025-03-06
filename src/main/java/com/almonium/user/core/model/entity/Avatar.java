package com.almonium.user.core.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
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
public class Avatar {

    @Id
    @UuidV7
    UUID id;

    @ManyToOne
    Profile profile;

    String url;

    public Avatar(Profile profile, String url) {
        this.profile = profile;
        this.url = url;
    }
}
