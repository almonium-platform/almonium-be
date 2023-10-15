package com.linguarium.user.model;

import com.linguarium.friendship.model.Friendship;
import com.linguarium.translator.model.Language;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Profile {

    @Id
    @Column(name = "id")
    Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    String background;
    String profilePicLink;
    int dailyGoal = 5;
    boolean friendshipRequestsBlocked;

    @Column(columnDefinition = "TIMESTAMP")
    LocalDateTime lastLogin;

    @Column
    int streak = 1;

    @Column
    Language uiLang = Language.EN;

    @OneToMany(mappedBy = "requestee")
    @OnDelete(action = OnDeleteAction.CASCADE)
    Set<Friendship> incomingFriendships;

    @OneToMany(mappedBy = "requester")
    @OnDelete(action = OnDeleteAction.CASCADE)
    Set<Friendship> outgoingFriendships;
}
