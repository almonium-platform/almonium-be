package com.linguarium.user.model;

import com.linguarium.friendship.model.Friendship;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

@NamedEntityGraph(
        name = "graph.User.details",
        attributeNodes = {
            @NamedAttributeNode(value = "learner", subgraph = "learner.details"),
            @NamedAttributeNode("profile")
        },
        subgraphs = {
            @NamedSubgraph(
                    name = "learner.details",
                    attributeNodes = {@NamedAttributeNode("targetLangs"), @NamedAttributeNode("fluentLangs")})
        })
@Entity
@Table(name = "auth")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    String providerUserId;

    @Column(nullable = false)
    String password;

    String provider;

    @Column(unique = true)
    String email;

    @Column(unique = true, nullable = false)
    String username;

    @Column(columnDefinition = "TIMESTAMP", nullable = false, updatable = false)
    @CreatedDate
    LocalDateTime registered;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    Profile profile;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    Learner learner;

    @OneToMany(mappedBy = "requestee")
    @OnDelete(action = OnDeleteAction.CASCADE)
    Set<Friendship> incomingFriendships;

    @OneToMany(mappedBy = "requester")
    @OnDelete(action = OnDeleteAction.CASCADE)
    Set<Friendship> outgoingFriendships;
}
