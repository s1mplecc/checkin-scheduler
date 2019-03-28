package com.caacetc.scheduling.plan.domain.shuttle;

public enum Period {
    MORNING("早"),
    AFTERNOON("中"),
    NIGHT("晚");

    private String description;

    Period(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
