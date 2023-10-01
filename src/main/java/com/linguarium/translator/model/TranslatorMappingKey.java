package com.linguarium.translator.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TranslatorMappingKey implements Serializable {
    Language sourceLang;
    Language targetLang;
    Long translatorId;
}
