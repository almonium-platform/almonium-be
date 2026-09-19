package com.almonium.subscription.model.entity.enums;

public enum PlanFeature {
    /**
     * How many languages may exist on the account. A storage and abuse ceiling, the same on every plan and never
     * shrinking: a downgraded account keeps every language it made, so a plan-shaped creation cap would leave it
     * permanently in violation and unable to re-add a language it already owns.
     */
    MAX_TARGET_LANGS,
    /** How many of those languages may be active at once. A plan entitlement, and the one a downgrade collects. */
    MAX_ACTIVE_LANGS,
    MAX_FLUENT_LANGS,
    /** How many private imports may stand on the shelf at a time. Deleting one frees its place; nothing resets. */
    MAX_BOOK_IMPORTS_ON_SHELF,
    MAX_TRANSLATION_REQUESTS_PER_MONTH,
}
