package com.linguatool.model.dto.lang.translation;

import com.linguatool.model.dto.api.response.yandex.YandexTranslationDto;
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
public class DefinitionDto {
    String text;
    String pos;
    String transcription;
    TranslationDto[] translations;
}
