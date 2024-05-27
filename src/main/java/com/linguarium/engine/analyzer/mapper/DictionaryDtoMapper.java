package com.linguarium.engine.analyzer.mapper;

import com.linguarium.engine.client.yandex.dto.YandexDefDto;
import com.linguarium.engine.client.yandex.dto.YandexDto;
import com.linguarium.engine.client.yandex.dto.YandexTranslationDto;
import com.linguarium.engine.translator.dto.DefinitionDto;
import com.linguarium.engine.translator.dto.TranslationCardDto;
import com.linguarium.engine.translator.dto.TranslationDto;
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
