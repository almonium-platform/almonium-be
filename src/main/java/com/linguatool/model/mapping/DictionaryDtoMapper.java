package com.linguatool.model.mapping;

import com.linguatool.model.dto.external_api.response.yandex.YandexDefDto;
import com.linguatool.model.dto.external_api.response.yandex.YandexDto;
import com.linguatool.model.dto.external_api.response.yandex.YandexTranslationDto;
import com.linguatool.model.dto.lang.translation.DefinitionDto;
import com.linguatool.model.dto.lang.translation.TranslationCardDto;
import com.linguatool.model.dto.lang.translation.TranslationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface DictionaryDtoMapper {
    @Mapping(source = "def", target = "definitions")
    TranslationCardDto yandexToGeneral(YandexDto dto);

    @Mapping(source = "ts", target = "transcription")
    @Mapping(source = "tr", target = "translations")
    DefinitionDto yandexDefinitionToGeneral(YandexDefDto dto);

    @Mapping(source = "fr", target = "frequency")
    TranslationDto yandexTranslationToGeneral(YandexTranslationDto dto);
}
