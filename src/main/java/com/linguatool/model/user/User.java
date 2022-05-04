package com.linguatool.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@NoArgsConstructor
@Data
@Table(name = "account")
public class User implements Serializable {

    private static final long serialVersionUID = 65981149772133526L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "PROVIDER_USER_ID")
    private String providerUserId;

    private String email;

    @Type(type = "numeric_boolean")
    private boolean enabled;

    @Column(name = "DISPLAY_NAME")
    private String displayName;

    @Column(columnDefinition = "TIMESTAMP", name = "created_date", nullable = false, updatable = false)
    protected LocalDateTime created;

    @Column(columnDefinition = "TIMESTAMP")
    protected LocalDateTime modified;

    private String password;

    private String provider;

    private boolean friendshipRequestsBlocked;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "user_role",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    private Set<Role> roles;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "requestee")
    private Set<Friendship> friendshipsInitiated;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "requester")
    private Set<Friendship> friendshipsRequested;

}
