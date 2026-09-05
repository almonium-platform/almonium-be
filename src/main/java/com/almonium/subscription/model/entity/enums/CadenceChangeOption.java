package com.almonium.subscription.model.entity.enums;

/**
 * The ways a member may move between monthly and annual billing.
 *
 * <p>The two directions are not symmetric. Upgrading prorates immediately because the credit is smaller than the
 * charge and settles on the card. Downgrading cannot: an immediate credit larger than the new charge lands on the
 * Paddle account balance rather than back on the card, leaving the member holding money they cannot see.
 */
public enum CadenceChangeOption {
    /** Monthly to annual. Credit for the unused month, difference charged now. */
    PRORATED_NOW,
    /** Annual to monthly. The item changes now, the new price bills at renewal, nothing is due today. */
    SCHEDULED,
    /** Annual to monthly inside the guarantee window. The annual payment is refunded and monthly starts today. */
    REFUND_AND_SWITCH
}
