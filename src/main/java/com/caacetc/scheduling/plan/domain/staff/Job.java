package com.caacetc.scheduling.plan.domain.staff;

import java.util.Arrays;

public enum Job {
    ECONOMY_CHECKIN("经济舱值机"),
    PREMIUM_CHECKIN("高端值机"),
    INVALID("非法");

    private String description;

    Job(String description) {
        this.description = description;
    }

    public static Job of(String cn) {
        return Arrays.stream(Job.values())
                .filter(job -> job.description().equals(cn))
                .findFirst()
                .orElse(INVALID);
    }

    public String description() {
        return description;
    }
}
