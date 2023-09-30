package com.linguarium.client.words.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class WordsReportDto {
    String word;
    Double frequency;
    WordsResultDto[] results;
    WordsSyllablesDto syllables;
    WordsPronunciationDto pronunciation;
}
