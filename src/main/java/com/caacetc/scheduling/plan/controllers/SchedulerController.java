package com.caacetc.scheduling.plan.controllers;


import com.caacetc.scheduling.plan.controllers.request.ScheduleRequest;
import com.caacetc.scheduling.plan.controllers.response.CounterResponse;
import com.caacetc.scheduling.plan.controllers.response.PassengerDistributionResponse;
import com.caacetc.scheduling.plan.controllers.response.StaffResponse;
import com.caacetc.scheduling.plan.domain.counter.Counter;
import com.caacetc.scheduling.plan.domain.counter.CounterScheduler;
import com.caacetc.scheduling.plan.domain.flight.Flight;
import com.caacetc.scheduling.plan.domain.flight.Interval;
import com.caacetc.scheduling.plan.domain.flight.PassengerDistribution;
import com.caacetc.scheduling.plan.domain.staff.Staff;
import com.caacetc.scheduling.plan.domain.staff.StaffScheduler;
import com.caacetc.scheduling.plan.gateway.FlightMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/schedule")
public class SchedulerController {

    @Resource
    private StaffScheduler staffScheduler;
    @Resource
    private PassengerDistribution passengerDistribution;
    @Resource
    private CounterScheduler counterScheduler;

    @GetMapping
    public String ok() {
        return "OK";
    }

    @GetMapping("/passengers/distribution")
    public List<PassengerDistributionResponse> passengerDistribution() {
        List<Flight> flights = new FlightMapper().flights();
        return passengerDistribution.estimate(flights).stream()
                .map(PassengerDistributionResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/counters")
    public List<CounterResponse> counters() {
        List<Flight> flights = new FlightMapper().flights();
        List<Interval> intervals = passengerDistribution.estimate(flights);
        return counterScheduler.schedule(intervals).stream()
                .map(CounterResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/staffs")
    public List<StaffResponse> staffs() {
        List<Flight> flights = new FlightMapper().flights();
        List<Interval> intervals = passengerDistribution.estimate(flights);
        List<Counter> counters = counterScheduler.schedule(intervals);
        return new StaffScheduler().schedule(counters).stream()
                .map(StaffResponse::new)
                .collect(Collectors.toList());
    }

    @PostMapping("/staffs")
    public List<StaffResponse> staffs2(@RequestBody ScheduleRequest scheduleRequest) {
        List<Flight> flights = scheduleRequest.flights();
        List<Staff> staffs = scheduleRequest.staffs();

        List<Interval> intervals = passengerDistribution.estimate(flights);
        List<Counter> counters = counterScheduler.schedule(intervals);

        // todo: schedule(counters, staffs1)
        List<Staff> staff = staffScheduler.schedule(counters);

        return staff.stream()
                .map(StaffResponse::new)
                .collect(Collectors.toList());
    }

}
