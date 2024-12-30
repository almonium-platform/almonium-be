package com.almonium.user.core.dto;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.user.core.model.enums.SetupStep;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class UserInfo {
    String id;
    String username;
    String email;
    String avatarUrl;

    boolean emailVerified;
    boolean isPremium;

    Integer streak;

    Collection<String> tags;
    Collection<Language> targetLangs; // todo remove
    Collection<LearnerDto> learners;
    Collection<Language> fluentLangs;

    SetupStep setupStep;
    SubscriptionInfoDto subscription;
}
