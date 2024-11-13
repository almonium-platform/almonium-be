package com.almonium.user.core.dto;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
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
    boolean emailVerified;
    Language uiLang;
    String profilePicLink;
    String background;
    Integer streak;
    Collection<Language> targetLangs;
    Collection<Language> fluentLangs;
    boolean setupCompleted;
    Collection<String> tags;
}
