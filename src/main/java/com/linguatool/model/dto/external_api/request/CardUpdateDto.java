package com.linguatool.model.dto.external_api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class CardUpdateDto {
    @NotNull
    Long id;
    String entry;
    TranslationDto[] translations;
    String notes;
    String source;
    String ipa;
    TagDto[] tags;
    ExampleDto[] examples;
    LocalDateTime created;
    LocalDateTime lastRepeat;
    Integer iteration;
    Long userId;
    Integer priority;
    int[] tr_del;
    int[] ex_del;
    LocalDateTime updated;
    Boolean activeLearning;
    Boolean falseFriend;
    Boolean irregularPlural;
    Boolean irregularSpelling;
    LanguageDto language;
}
