package com.linguatool.model.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linguatool.model.entity.lang.Card;
import com.linguatool.model.entity.lang.CardSuggestion;
import com.linguatool.model.entity.lang.Language;
import com.linguatool.model.entity.lang.LanguageEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static com.linguatool.util.GeneralUtils.generateId;


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

    private String profilePicLink;

    @Column(unique = true)
    private String email;

    @Type(type = "numeric_boolean")
    private boolean enabled;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(columnDefinition = "TIMESTAMP", name = "created_date", nullable = false, updatable = false)
    protected LocalDateTime created;

    @Column(columnDefinition = "TIMESTAMP")
    protected LocalDateTime modified;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime lastLogin;

    @Column
    private int streak = 1;

    private String password;

    private String background;

    private String provider;

    private int dailyGoal = 5;
    private double vocabularyLevel = 5.5;

    @Column(name = "ui_lang")
    private Language uiLanguage = Language.ENGLISH;

    private boolean friendshipRequestsBlocked;

    @JsonIgnore
    @ManyToMany
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    private Set<Role> roles;

    @OneToMany(mappedBy = "requestee", fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Friendship> friendshipsInitiated;

    @OneToMany(mappedBy = "requester", fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Friendship> friendshipsRequested;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Card> cards;

    @OneToMany(mappedBy = "sender")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<CardSuggestion> suggestedByMe;

    @OneToMany(mappedBy = "recipient")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<CardSuggestion> suggestedToMe;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(name = "user_target_lang",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "lang_id", referencedColumnName = "id")}
    )
    Set<LanguageEntity> targetLanguages;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(name = "user_fluent_lang",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "lang_id", referencedColumnName = "id")}
    )
    Set<LanguageEntity> fluentLanguages;


    public void addCard(Card card) {
        if (card != null) {
            this.cards.add(card);
            card.setOwner(this);
        }
    }

    public void addCards(Iterable<Card> cards) {
        cards.forEach(this::addCard);
    }

    public void removeCard(Card card) {
        if (card != null) {
            this.cards.remove(card);
            card.setOwner(null);
        }
    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//
//        if (o == null || getClass() != o.getClass()) {
//            return false;
//        }
//
//        User user = (User) o;
//
//        return new EqualsBuilder().append(enabled, user.enabled).append(friendshipRequestsBlocked, user.friendshipRequestsBlocked).append(id, user.id).append(providerUserId, user.providerUserId).append(email, user.email).append(username, user.username).append(created, user.created).append(modified, user.modified).append(password, user.password).append(provider, user.provider).append(uiLanguage, user.uiLanguage).append(roles, user.roles).append(friendshipsInitiated, user.friendshipsInitiated).append(cards, user.cards).isEquals();
//    }
//
//    @Override
//    public int hashCode() {
//        return new HashCodeBuilder(17, 37).append(id).append(providerUserId).append(email).append(enabled).append(username).append(created).append(modified).append(password).append(provider).append(uiLanguage).append(friendshipRequestsBlocked).append(roles).toHashCode();
//    }

    @PrePersist
    void usernameGenerator() {
        if (this.username == null) {
            this.username = generateId();
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
