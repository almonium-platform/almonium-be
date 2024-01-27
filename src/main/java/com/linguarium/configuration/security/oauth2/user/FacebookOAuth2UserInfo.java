package com.linguarium.configuration.security.oauth2.user;

import java.util.Map;

public class FacebookOAuth2UserInfo extends OAuth2UserInfo {
    public FacebookOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return getStringAttribute("id");
    }

    @Override
    public String getName() {
        return getStringAttribute("name");
    }

    @Override
    public String getFirstName() {
        return getStringAttribute("first_name");
    }

    @Override
    public String getLastName() {
        return getStringAttribute("last_name");
    }

    @Override
    public String getEmail() {
        return getStringAttribute("email");
    }

    @Override
    public String getImageUrl() {
        return getNestedStringAttribute("picture.data.url");
    }

    private String getStringAttribute(String attributeName) {
        Object value = attributes.get(attributeName);
        return (value instanceof String) ? (String) value : null;
    }

    private String getNestedStringAttribute(String nestedAttributeName) {
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
