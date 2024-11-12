package com.almonium.user.core.dto;

import com.almonium.analyzer.translator.model.enums.Language;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private String id;
    private String username;
    private String email;
    private Language uiLang;
    private String profilePicLink;
    private String background;
    private Integer streak;
    private Collection<Language> targetLangs;
    private Collection<Language> fluentLangs;
    private boolean setupCompleted;
    private Collection<String> tags;
}
