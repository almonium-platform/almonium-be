package com.almonium.user.core.model.enums;

public enum SetupStep {
    // order of declaration is important!
    WELCOME,
    PLAN,
    LANGUAGES,
    PROFILE,
    INTERESTS,
    COMPLETED;

    public static SetupStep getInitial() {
        return WELCOME;
    }

    public boolean isNext(SetupStep step) {
        return this.ordinal() == step.ordinal() + 1;
    }

    public boolean isUnreachable(SetupStep step) {
        return this.ordinal() > step.ordinal() + 1;
    }

    public SetupStep nextStep() {
        if (this == COMPLETED) {
            throw new IllegalStateException("This is the last step");
        }
        return values()[ordinal() + 1];
    }
}
