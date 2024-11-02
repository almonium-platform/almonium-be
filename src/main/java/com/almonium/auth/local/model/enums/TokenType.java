package com.almonium.auth.local.model.enums;

import com.almonium.infra.email.model.enums.EmailTemplateType;

public enum TokenType implements EmailTemplateType {
    EMAIL_VERIFICATION,
    PASSWORD_RESET,
    EMAIL_CHANGE_VERIFICATION,
}
