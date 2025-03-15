package com.almonium.user.core.dto.response;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.user.core.dto.TargetLanguageWithProficiency;
import java.util.Collection;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class FullProfileInfo extends BaseProfileInfo {
    Collection<Language> fluentLangs;
    Collection<TargetLanguageWithProficiency> targetLangs;
    Collection<String> interests;
    int loginStreak;
    UUID friendshipId;

    public FullProfileInfo(BaseProfileInfo baseProfileInfo) {
        super(
                baseProfileInfo.getId(),
                baseProfileInfo.getUsername(),
                baseProfileInfo.getAvatarUrl(),
                baseProfileInfo.getRegisteredAt(),
                baseProfileInfo.isPremium(),
                baseProfileInfo.getAcceptsRequests(),
                baseProfileInfo.getRelationshipStatus(),
                false);
    }
}
