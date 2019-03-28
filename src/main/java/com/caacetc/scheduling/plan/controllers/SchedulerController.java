package com.caacetc.scheduling.plan.controllers;


import com.caacetc.scheduling.plan.controllers.request.FlightReq;
import com.caacetc.scheduling.plan.controllers.request.StaffReq;
import com.caacetc.scheduling.plan.controllers.response.CounterRes;
import com.caacetc.scheduling.plan.controllers.response.PassengerDistributionRes;
import com.caacetc.scheduling.plan.controllers.response.StaffRes;
import com.caacetc.scheduling.plan.domain.Flight;
import com.caacetc.scheduling.plan.domain.Interval;
import com.caacetc.scheduling.plan.domain.PassengerDistribution;
import com.caacetc.scheduling.plan.domain.counter.Counter;
import com.caacetc.scheduling.plan.domain.counter.CounterScheduler;
import com.caacetc.scheduling.plan.domain.staff.Staff;
import com.caacetc.scheduling.plan.domain.staff.StaffScheduler;
import com.caacetc.scheduling.plan.mapper.FlightMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<PassengerDistributionRes> passengerDistribution() {
        List<Flight> flights = new FlightMapper().flights();
        return passengerDistribution.estimate(flights).stream()
                .map(PassengerDistributionRes::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/counters")
    public List<CounterRes> counters() {
        List<Flight> flights = new FlightMapper().flights();
        List<Interval> intervals = passengerDistribution.estimate(flights);
        return counterScheduler.schedule(intervals).stream()
                .map(CounterRes::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/staffs")
    public List<StaffRes> staffs() {
        List<Flight> flights = new FlightMapper().flights();
        List<Interval> intervals = passengerDistribution.estimate(flights);
        List<Counter> counters = counterScheduler.schedule(intervals);
        return new StaffScheduler().schedule(counters).stream()
                .map(StaffRes::new)
                .collect(Collectors.toList());
    }

    @PostMapping("/staffs")
    public List<StaffRes> staffs2(List<FlightReq> flights, List<StaffReq> staffs) {
        List<Flight> flights1 = flights.stream().map(Flight::new).collect(Collectors.toList());
        List<Staff> staffs1 = staffs.stream().map(Staff::new).collect(Collectors.toList());

        List<Interval> intervals = passengerDistribution.estimate(flights1);
        List<Counter> counters = counterScheduler.schedule(intervals);

        // todo: schedule(counters, staffs1)
        List<Staff> staff = staffScheduler.schedule(counters);

        return staff.stream()
                .map(StaffRes::new)
                .collect(Collectors.toList());
    }
}
