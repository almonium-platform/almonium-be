package com.almonium.learning.book.dto.response;

import com.almonium.analyzer.translator.model.enums.Language;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookMiniDetails {
    Long id;
    Language language;
}
