package com.linguarium.user.model;

import javax.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

import static com.linguarium.util.GeneralUtils.generateId;

@Entity
@Table(name = "user_core")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"email"})
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
    String username = generateId();

    @Column(columnDefinition = "TIMESTAMP", nullable = false, updatable = false)
    @CreatedDate
    LocalDateTime registered;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Profile profile;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Learner learner;
}
