package com.linguatool.model.entity.user;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

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

        throw new IllegalArgumentException("Unknown database value:" + dbData);
    }
}
