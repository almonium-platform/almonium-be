package com.almonium.user.core.dto.response;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.user.core.model.enums.SetupStep;
import java.util.Collection;
import java.util.Map;
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
    String streamChatToken;

    boolean emailVerified;
    boolean isPremium;
    boolean hidden;

    Integer streak;

    Collection<String> tags;
    Collection<LearnerDto> learners;
    Collection<Language> fluentLangs;
    Collection<InterestDto> interests;

    SetupStep setupStep;
    SubscriptionInfoDto subscription;
    Map<String, Object> uiPreferences;
}
