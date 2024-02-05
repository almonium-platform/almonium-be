package com.linguarium.translator.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TranslatorMappingKey implements Serializable {
    private Language sourceLang;
    private Language targetLang;
    private Long translatorId;
}
