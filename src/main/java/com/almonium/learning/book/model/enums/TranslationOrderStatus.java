package com.almonium.learning.book.model.enums;

/**
 * A request is only ever asked or settled. Withdrawing deletes the row outright, so
 * the caller's list never has to explain a state they did not choose.
 */
public enum TranslationOrderStatus {
    ASKED,
    READY,
    DECLINED,
}
