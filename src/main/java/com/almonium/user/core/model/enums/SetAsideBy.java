package com.almonium.user.core.model.enums;

/**
 * Who put a language down. A downgrade may take languages away, but only what it took may be handed back on
 * re-upgrade: a language the user set aside deliberately stays set aside.
 */
public enum SetAsideBy {
    USER,
    SYSTEM
}
