package linguarium.auth.social.model.userinfo;

import java.util.Map;
import linguarium.auth.common.enums.AuthProviderType;

public class FacebookOAuth2UserInfo extends OAuth2UserInfo {
    public FacebookOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public AuthProviderType getProvider() {
        return AuthProviderType.FACEBOOK;
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
    public String getEmail() {
        return getStringAttribute("email");
    }

    @Override
    public String getImageUrl() {
        return getNestedStringAttribute("picture.data.url");
    }
}
