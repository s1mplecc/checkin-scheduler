package com.caacetc.scheduling.plan.domain.counter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class OpenPeriod {
    private final List<OpenFragment> openFragments;

    public OpenPeriod() {
        openFragments = new ArrayList<>();
    }

    public void add(OpenFragment openFragment) {
        AtomicBoolean canCombine = new AtomicBoolean(false);
        openFragments.stream()
                .filter(o -> o.endTime().isEqual(openFragment.startTime()))
                .findFirst()
                .ifPresent(o -> {
                    o.setEndTime(o.endTime().plusHours(1));
                    canCombine.set(true);
                });

        if (!canCombine.get()) {
            openFragments.add(openFragment);
        }
    }

    public List<OpenFragment> openFragments() {
        return openFragments;
    }
}
