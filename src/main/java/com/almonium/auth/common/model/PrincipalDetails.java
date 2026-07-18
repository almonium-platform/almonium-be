package com.almonium.auth.common.model;

import java.util.UUID;

public interface PrincipalDetails {
    UUID getUserId();

    String getEmail();
}
