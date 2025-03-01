package com.almonium.user.friendship.model.enums;

import com.almonium.infra.email.model.enums.EmailTemplateType;

public enum FriendshipEvent implements EmailTemplateType {
    INITIATED,
    ACCEPTED,
}
