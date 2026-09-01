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
    MAX_BOOK_IMPORTS_PER_MONTH,
    MAX_TRANSLATION_REQUESTS_PER_MONTH,
}
