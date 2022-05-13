package com.linguatool.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;


@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "account")
public class User implements Serializable {

    private static final long serialVersionUID = 65981149772133526L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "PROVIDER_USER_ID")
    private String providerUserId;

    @Column(unique = true)
    private String email;

    @Type(type = "numeric_boolean")
    private boolean enabled;

    @Column(name = "username", unique = true)
    private String username;

    @Column(columnDefinition = "TIMESTAMP", name = "created_date", nullable = false, updatable = false)
    protected LocalDateTime created;

    @Column(columnDefinition = "TIMESTAMP")
    protected LocalDateTime modified;

    private String password;

    private String provider;

    @Column(name = "ui_lang")
    private Language uiLanguage = Language.ENGLISH;

    private boolean friendshipRequestsBlocked;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "user_role",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    private Set<Role> roles;

    @OneToMany(mappedBy = "requestee")
    private Set<Friendship> friendshipsInitiated;

    @OneToMany(mappedBy = "requester")
    private Set<Friendship> friendshipsRequested;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return enabled == user.enabled && friendshipRequestsBlocked == user.friendshipRequestsBlocked && Objects.equals(id, user.id) && Objects.equals(providerUserId, user.providerUserId) && Objects.equals(email, user.email) && Objects.equals(username, user.username) && Objects.equals(created, user.created) && Objects.equals(modified, user.modified) && Objects.equals(password, user.password) && Objects.equals(provider, user.provider) && Objects.equals(roles, user.roles) && Objects.equals(friendshipsInitiated, user.friendshipsInitiated) && Objects.equals(friendshipsRequested, user.friendshipsRequested);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, providerUserId, email, enabled, username, created, modified, password, provider, friendshipRequestsBlocked, roles, friendshipsInitiated, friendshipsRequested);
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", providerUserId='" + providerUserId + '\'' +
            ", email='" + email + '\'' +
            ", enabled=" + enabled +
            ", username='" + username + '\'' +
            ", created=" + created +
            ", modified=" + modified +
            ", password='" + password + '\'' +
            ", provider='" + provider + '\'' +
            ", friendshipRequestsBlocked=" + friendshipRequestsBlocked +
            '}';
    }
}
