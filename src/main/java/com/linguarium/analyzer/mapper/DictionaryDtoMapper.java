package com.linguarium.analyzer.mapper;

import com.linguarium.client.yandex.dto.YandexDefDto;
import com.linguarium.client.yandex.dto.YandexDto;
import com.linguarium.client.yandex.dto.YandexTranslationDto;
import com.linguarium.translator.dto.DefinitionDto;
import com.linguarium.translator.dto.TranslationCardDto;
import com.linguarium.translator.dto.TranslationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface DictionaryDtoMapper {
    @Mapping(source = "def", target = "definitions")
    TranslationCardDto yandexToGeneral(YandexDto dto);

    @Mapping(source = "ts", target = "transcription")
    @Mapping(source = "tr", target = "translations")
    DefinitionDto yandexDefinitionToGeneral(YandexDefDto dto);

    @Mapping(source = "fr", target = "frequency")
    TranslationDto yandexTranslationToGeneral(YandexTranslationDto dto);
}
