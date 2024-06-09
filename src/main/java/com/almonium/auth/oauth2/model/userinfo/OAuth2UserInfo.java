package com.almonium.auth.oauth2.model.userinfo;

import com.almonium.auth.common.enums.AuthProviderType;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;

    public abstract AuthProviderType getProvider();

    public abstract String getId();

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getImageUrl();

    public String getStringAttribute(String attributeName) {
        Object value = attributes.get(attributeName);
        return (value instanceof String) ? (String) value : null;
    }

    public String getNestedStringAttribute(String nestedAttributeName) {
        String[] nestedAttributes = nestedAttributeName.split("\\.");
        Object currentObj = attributes;
        for (String attr : nestedAttributes) {
            if (currentObj instanceof Map) {
                currentObj = ((Map<?, ?>) currentObj).get(attr);
            } else {
                return null;
            }
        }
        return (currentObj instanceof String) ? (String) currentObj : null;
    }
}
