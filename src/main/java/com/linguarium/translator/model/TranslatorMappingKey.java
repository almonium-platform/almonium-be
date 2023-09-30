package com.linguarium.translator.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TranslatorMappingKey implements Serializable {
    Long sourceLangId;
    Long targetLangId;
    Long translatorId;
}
