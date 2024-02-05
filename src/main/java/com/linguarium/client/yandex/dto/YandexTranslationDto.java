package com.linguarium.client.yandex.dto;

import static lombok.AccessLevel.PRIVATE;

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
public class YandexTranslationDto {
    String text;
    String pos;
    Integer fr;
    String gen;
    YandexSynDto[] syn;
    YandexMeanDto[] mean;
}
