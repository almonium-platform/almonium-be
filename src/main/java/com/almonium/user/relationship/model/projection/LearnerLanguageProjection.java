package com.almonium.user.relationship.model.projection;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/** One language a person is studying, carried back with the id it belongs to so a batch can be regrouped. */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class LearnerLanguageProjection {
    UUID userId;
    Language language;
}
