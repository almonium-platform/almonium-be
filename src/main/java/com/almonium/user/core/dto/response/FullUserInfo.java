package com.almonium.user.core.dto.response;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.user.core.dto.TargetLanguageWithProficiency;
import java.util.Collection;
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
public class FullUserInfo extends BaseUserInfo {
    Collection<Language> fluentLangs;
    Collection<TargetLanguageWithProficiency> targetLangs;
    Collection<String> interests;
    int loginStreak;

    public FullUserInfo(BaseUserInfo fullUserInfo) {
        super(
                fullUserInfo.getId(),
                fullUserInfo.getUsername(),
                fullUserInfo.getAvatarUrl(),
                fullUserInfo.getRegisteredAt(),
                fullUserInfo.isPremium(),
                false);
    }
}
