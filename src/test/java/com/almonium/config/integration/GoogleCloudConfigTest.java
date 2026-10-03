package com.almonium.config.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.almonium.config.properties.GoogleProperties;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.translate.v3.LocationName;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GoogleCloudConfigTest {

    GoogleCloudConfig googleCloudConfig;

    @BeforeEach
    void setUp() {
        GoogleProperties googleProperties = new GoogleProperties();
        googleProperties.setParentLocation("global");
        googleCloudConfig = new GoogleCloudConfig(googleProperties);
    }

    @DisplayName("Should take the Translation project from the service-account key")
    @Test
    void givenServiceAccountKey_whenLocationName_thenUseKeyProject() throws NoSuchAlgorithmException {
        GoogleCredentials credentials = ServiceAccountCredentials.newBuilder()
                .setProjectId("almonium-dev")
                .setClientEmail("account@almonium-dev.iam.gserviceaccount.com")
                .setPrivateKey(
                        KeyPairGenerator.getInstance("RSA").generateKeyPair().getPrivate())
                .build();

        LocationName locationName = googleCloudConfig.locationName(credentials);

        assertThat(locationName.toString()).isEqualTo("projects/almonium-dev/locations/global");
    }

    @DisplayName("Should refuse credentials that name no project")
    @Test
    void givenCredentialsWithoutProject_whenLocationName_thenThrow() {
        GoogleCredentials credentials = GoogleCredentials.create(new AccessToken("token", null));

        assertThatThrownBy(() -> googleCloudConfig.locationName(credentials)).isInstanceOf(IllegalStateException.class);
    }
}
