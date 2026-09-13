package com.almonium.learning.book.model.enums;

/** The four plain mails the reading features send; each maps to a template under {@code email-templates/books}. */
public enum BookEmailType {
    TRANSLATION_READY,
    TRANSLATION_DECLINED,
    SUGGESTION_PUBLISHED,
    SUGGESTION_DECLINED
}
