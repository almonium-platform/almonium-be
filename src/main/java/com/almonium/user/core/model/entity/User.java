package com.almonium.user.core.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.auth.common.model.entity.Principal;
import com.almonium.subscription.model.entity.PlanSubscription;
import com.almonium.user.core.model.enums.SetupStep;
import com.almonium.user.relationship.model.entity.Relationship;
import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@NamedEntityGraph(
        name = "graph.User.details",
        attributeNodes = {
            @NamedAttributeNode("profile"),
            @NamedAttributeNode("planSubscriptions"),
            @NamedAttributeNode("fluentLangs"),
            @NamedAttributeNode("interests")
        })
@Entity
@Table(name = "user_core")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @UuidV7
    UUID id;

    String email;

    boolean emailVerified;

    String username;

    String stripeCustomerId;

    String streamChatToken;

    @Enumerated(EnumType.STRING)
    SetupStep setupStep;

    @CreatedDate // todo: add At
    Instant registered;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    Profile profile;

    @Builder.Default
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    Set<Principal> principals = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    Set<Learner> learners = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "requestee")
    Set<Relationship> incomingRelationships = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "requester")
    Set<Relationship> outgoingRelationships = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    Set<PlanSubscription> planSubscriptions = new HashSet<>();

    @ElementCollection(targetClass = Language.class)
    @CollectionTable(name = "user_fluent_lang", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    Set<Language> fluentLangs;

    @ManyToMany
    @JoinTable(
            name = "user_interest",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "interest_id"))
    Set<Interest> interests;

    @SneakyThrows
    @PrePersist
    private void prePersist() {
        if (profile == null) {
            profile =
                    Profile.builder().user(this).lastLogin(LocalDateTime.now()).build();
        }
    }
}
