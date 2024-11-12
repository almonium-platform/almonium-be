package com.almonium.analyzer.client.exception;

import com.almonium.analyzer.translator.model.enums.Language;

public class ApiNoLangSupportException extends IllegalStateException {
    public ApiNoLangSupportException(Language language, String api) {
        super(String.format("Language %s is not supported by %s integration", language, api));
    }
}
