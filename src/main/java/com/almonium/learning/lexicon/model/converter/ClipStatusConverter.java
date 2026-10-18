package com.almonium.learning.lexicon.model.converter;

import com.almonium.learning.lexicon.model.enums.ClipStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/** Stores a clip's status as its lowercase label, like every other status in the lexicon tables. */
@Converter(autoApply = true)
public class ClipStatusConverter implements AttributeConverter<ClipStatus, String> {
    @Override
    public String convertToDatabaseColumn(ClipStatus status) {
        return status == null ? null : status.label();
    }

    @Override
    public ClipStatus convertToEntityAttribute(String label) {
        return label == null ? null : ClipStatus.fromLabel(label);
    }
}
