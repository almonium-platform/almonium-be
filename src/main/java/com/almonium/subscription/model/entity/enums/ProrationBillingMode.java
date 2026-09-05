package com.almonium.subscription.model.entity.enums;

/**
 * How Paddle bills a mid-cycle subscription item change.
 *
 * <p>The wire values live here and nowhere else. Paddle has renamed this field before, so a rename is meant to be a
 * one-line edit rather than a hunt through request bodies.
 */
public enum ProrationBillingMode {
    /** Credit the unused remainder, charge the difference now. The upgrade direction. */
    PRORATED_IMMEDIATELY("prorated_immediately"),
    /** Charge the new price in full now and restart the billing period. Used with a refund, never alone. */
    FULL_IMMEDIATELY("full_immediately"),
    /** Change the item now, bill the new price at the next renewal. The downgrade direction. */
    FULL_NEXT_BILLING_PERIOD("full_next_billing_period"),
    /** Change the item and bill nothing. How a pending change is undone. */
    DO_NOT_BILL("do_not_bill");

    private final String wireValue;

    ProrationBillingMode(String wireValue) {
        this.wireValue = wireValue;
    }

    public String wireValue() {
        return wireValue;
    }
}
