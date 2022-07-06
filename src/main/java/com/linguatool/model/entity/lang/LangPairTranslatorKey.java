package com.linguatool.model.entity.lang;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class LangPairTranslatorKey implements Serializable {
    Long langFromId;
    Long langToId;
    Long translatorId;

}
