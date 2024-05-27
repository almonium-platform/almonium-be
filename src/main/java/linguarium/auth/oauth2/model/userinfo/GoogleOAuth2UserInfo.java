package linguarium.auth.oauth2.model.userinfo;

import java.util.Map;
import linguarium.auth.oauth2.model.enums.AuthProviderType;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {
    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public AuthProviderType getProvider() {
        return AuthProviderType.GOOGLE;
    }

    @Override
    public String getId() {
        return getStringAttribute("sub");
    }

    @Override
    public String getName() {
        return getStringAttribute("name");
    }

    @Override
    public String getFirstName() {
        return getStringAttribute("given_name");
    }

    @Override
    public String getLastName() {
        return getStringAttribute("family_name");
    }

    @Override
    public String getEmail() {
        return getStringAttribute("email");
    }

    @Override
    public String getImageUrl() {
        return getStringAttribute("picture");
    }
}
