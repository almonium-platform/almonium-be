package com.linguatool.model.dto.external_api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class CardCreationDto {

    @NotBlank
    String entry;
    @NotEmpty
    TranslationDto[] translations;
    String notes;
    TagDto[] tags;
    ExampleDto[] examples;
    boolean activeLearning;
    boolean irregularPlural;
    boolean falseFriend;
    boolean irregularSpelling;
    boolean learnt;
    @NotNull
    LanguageDto language;
    String created;
    String updated;
    String lastRepeat;
    Integer priority;
}
