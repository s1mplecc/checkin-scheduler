package com.caacetc.scheduling.plan.domain.counter;

import com.caacetc.scheduling.plan.domain.flight.Flight;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FlightDateTime {
    private final Set<OneDay> days;

    public FlightDateTime() {
        this.days = new HashSet<>();
    }

    public void of(List<Flight> flights) {
    }

    public class OneDay {
        private String date;
        private LocalDateTime domEndTime;
        private LocalDateTime intEndTime;

        public String date() {
            return date;
        }

        public LocalDateTime domEndTime() {
            return domEndTime;
        }

        public LocalDateTime intEndTime() {
            return intEndTime;
        }

        @Override
        public int hashCode() {
            return date.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return date.equals(((OneDay) obj).date());
        }
    }
}
