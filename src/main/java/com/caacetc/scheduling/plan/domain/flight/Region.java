package com.caacetc.scheduling.plan.domain.flight;

public enum Region {
    DOM,
    INT,
    MIX;

    public boolean isDom() {
        return this.equals(DOM);
    }

    public boolean isInt() {
        return this.equals(INT);
    }

    public boolean isMix() {
        return this.equals(MIX);
    }
}
