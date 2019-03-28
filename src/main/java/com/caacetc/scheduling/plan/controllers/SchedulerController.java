package com.caacetc.scheduling.plan.controllers;


import com.caacetc.scheduling.plan.controllers.response.CounterRes;
import com.caacetc.scheduling.plan.controllers.response.PassengerDistributionRes;
import com.caacetc.scheduling.plan.controllers.response.StaffRes;
import com.caacetc.scheduling.plan.domain.Flight;
import com.caacetc.scheduling.plan.domain.Interval;
import com.caacetc.scheduling.plan.domain.PassengerDistribution;
import com.caacetc.scheduling.plan.domain.counter.Counter;
import com.caacetc.scheduling.plan.domain.counter.CounterScheduler;
import com.caacetc.scheduling.plan.domain.staff.StaffScheduler;
import com.caacetc.scheduling.plan.mapper.FlightMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/schedule")
public class SchedulerController {

    @GetMapping
    public String ok() {
        return "OK";
    }

    @GetMapping("/passengers/distribution")
    public List<PassengerDistributionRes> passengerDistribution() {
        List<Flight> flights = new FlightMapper().flights();
        return new PassengerDistribution().estimate(flights).stream()
                .map(PassengerDistributionRes::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/counters")
    public List<CounterRes> counters() {
        List<Flight> flights = new FlightMapper().flights();
        List<Interval> intervals = new PassengerDistribution().estimate(flights);
        return new CounterScheduler().schedule(intervals).stream()
                .map(CounterRes::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/staffs")
    public List<StaffRes> staffs() {
        List<Flight> flights = new FlightMapper().flights();
        List<Interval> intervals = new PassengerDistribution().estimate(flights);
        List<Counter> counters = new CounterScheduler().schedule(intervals);
        return new StaffScheduler().schedule(counters).stream()
                .map(StaffRes::new)
                .collect(Collectors.toList());
    }
}
