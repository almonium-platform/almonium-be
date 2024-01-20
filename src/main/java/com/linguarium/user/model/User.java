package com.linguarium.user.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

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
}
