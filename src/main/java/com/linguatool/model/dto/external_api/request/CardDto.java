package com.linguatool.model.dto.external_api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class CardDto {
    Long id;
    @NotBlank
    String entry;
    @NotEmpty
    TranslationDto[] translations;
    String notes;
    String source;
    String ipa;
    TagDto[] tags;
    ExampleDto[] examples;
    LocalDateTime created;
    LocalDateTime lastRepeat;
    int iteration;
    Long userId;
    int priority;
    LocalDateTime updated;
    boolean activeLearning;
    boolean irregularPlural;
    boolean irregularSpelling;
    boolean falseFriend;
    @NotNull
    LanguageDto language;
}
