package com.almonium.user.relationship.dto.response;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@AllArgsConstructor
public class PublicUserProfile {
    UUID id;
    String username;
    String avatarUrl;
    boolean premium;

    /**
     * The languages this person is studying, so a row in the People panel can say who they are rather than only what
     * they are called. Left empty by every query: only RelationshipService knows whether the profile is hidden, and a
     * hidden profile says nothing. See {@link com.almonium.user.relationship.service.RelationshipService}.
     */
    List<Language> learning = new ArrayList<>();

    public PublicUserProfile(UUID id, String username, String avatarUrl, boolean premium) {
        this.id = id;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.premium = premium;
    }
}
