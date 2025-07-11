package com.almonium.learning.book.dto.response;

import com.almonium.analyzer.translator.model.enums.Language;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookMiniDetails {
    int progressPercentage;
    Language language;
    List<BookLanguageVariant> languageVariants;
}
