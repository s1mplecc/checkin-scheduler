package com.caacetc.scheduling.plan.checkin.domain;

import java.util.regex.Pattern;

/**
 * 舱位等级，其中 G、Y 在经济舱值机柜台值机，其余均在高端值机柜台值机
 */
public enum CabinRank {
    P("豪华头等舱"),
    F("头等舱"),
    C("公务舱"),
    J("公务舱"),
    G("超级经济舱"),
    Y("经济舱");

    private String description;

    CabinRank(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }

    public Pattern pattern() {
        String regex = ".*?(\\[" + this + ":(\\d+),(\\d+),(\\d+)\\]).*?";
        return Pattern.compile(regex);
    }

    public boolean isEconomy() {
        return this.equals(G) || this.equals(Y);
    }
}
