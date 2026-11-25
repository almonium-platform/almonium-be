package com.almonium.config.properties;

import static lombok.AccessLevel.PRIVATE;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/** What bounds the spend on Discover's "word vs word" explanations. The model and its key are Almo's. */
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "lexicon.comparison")
@FieldDefaults(level = PRIVATE)
public class SenseComparisonProperties {
    /** New explanations one member may buy per rolling day; one already bought is served past it. */
    @Min(1)
    int dailyCeiling = 30;
}
