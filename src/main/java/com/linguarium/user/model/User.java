package com.linguarium.user.model;

import com.linguarium.friendship.model.Friendship;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@NamedEntityGraph(
        name = "graph.User.details",
        attributeNodes = {
                @NamedAttributeNode(value = "learner", subgraph = "learner.details"),
                @NamedAttributeNode("profile")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "learner.details",
                        attributeNodes = {
                                @NamedAttributeNode("targetLangs"),
                                @NamedAttributeNode("fluentLangs")
                        }
                )
        }
)
@Entity
@Table(name = "user_core")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User implements Serializable {

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
    private Profile profile;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Learner learner;

    @OneToMany(mappedBy = "requestee")
    @OnDelete(action = OnDeleteAction.CASCADE)
    Set<Friendship> incomingFriendships;

    @OneToMany(mappedBy = "requester")
    @OnDelete(action = OnDeleteAction.CASCADE)
    Set<Friendship> outgoingFriendships;
}
