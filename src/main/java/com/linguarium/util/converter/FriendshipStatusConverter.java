package com.linguarium.util.converter;

import com.linguarium.friendship.model.FriendshipStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class FriendshipStatusConverter implements AttributeConverter<FriendshipStatus, String> {

    @Override
    public String convertToDatabaseColumn(FriendshipStatus nodeType) {
        return nodeType.getCode();
    }

    @Override
    public FriendshipStatus convertToEntityAttribute(String dbData) {
        for (FriendshipStatus nodeType : FriendshipStatus.values()) {
            if (nodeType.getCode().equals(dbData)) {
                return nodeType;
            }
        }

        throw new IllegalArgumentException("Unknown database value: " + dbData);
    }
}
