package com.caacetc.scheduling.plan.domain.flight;

import lombok.Setter;

import java.time.Instant;

import static com.caacetc.scheduling.plan.domain.flight.Region.DOM;
import static com.caacetc.scheduling.plan.domain.flight.Region.INT;

@Setter
public class Flight implements Comparable<Flight> {
    private Instant departTime;
    private Region region;
    private int economyCabinNum;
    private int premiumCabinNum;

    public Instant departTime() {
        return departTime;
    }

    public int domEconomyCabinNum() {
        if (region.isDom() || region.isMix()) {
            return economyCabinNum;
        } else {
            return 0;
        }
    }

    public int intEconomyCabinNum() {
        if (region.isInt() || region.isMix()) {
            return economyCabinNum;
        } else {
            return 0;
        }
    }

    public int premiumCabinNum() {
        return premiumCabinNum;
    }

    public boolean isDom() {
        return DOM.equals(region);
    }

    public boolean isInt() {
        return INT.equals(region);
    }

    @Override
    public int compareTo(Flight another) {
        return departTime.compareTo(another.departTime);
    }
}
