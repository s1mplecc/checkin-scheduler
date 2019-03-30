package com.caacetc.scheduling.plan.controllers;


import com.caacetc.scheduling.plan.controllers.request.ScheduleRequest;
import com.caacetc.scheduling.plan.controllers.response.CounterResponse;
import com.caacetc.scheduling.plan.controllers.response.PassengerDistributionResponse;
import com.caacetc.scheduling.plan.controllers.response.StaffResponse;
import com.caacetc.scheduling.plan.domain.counter.Counter;
import com.caacetc.scheduling.plan.domain.counter.CounterScheduler;
import com.caacetc.scheduling.plan.domain.flight.Flight;
import com.caacetc.scheduling.plan.domain.flight.PassengerCalculator;
import com.caacetc.scheduling.plan.domain.flight.PassengerDistribution;
import com.caacetc.scheduling.plan.domain.staff.Staff;
import com.caacetc.scheduling.plan.domain.staff.StaffScheduler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    private PassengerCalculator passengerCalculator;
    @Resource
    private CounterScheduler counterScheduler;

    @PostMapping("/passengers/distribution")
    public List<PassengerDistributionResponse> passengerDistribution(@RequestBody ScheduleRequest scheduleRequest) {
        List<Flight> flights = scheduleRequest.flights();
        return passengerCalculator.estimateBy(flights).stream()
                .map(PassengerDistributionResponse::new)
                .collect(Collectors.toList());
    }

    @PostMapping("/counters")
    public List<CounterResponse> counters(@RequestBody ScheduleRequest scheduleRequest) {
        List<Flight> flights = scheduleRequest.flights();
        List<PassengerDistribution> distributions = passengerCalculator.estimateBy(flights);
        return counterScheduler.scheduleBy(distributions, flights).stream()
                .map(CounterResponse::new)
                .filter(counterResponse -> !counterResponse.getOpenPeriods().isEmpty())
                .collect(Collectors.toList());
    }

    @PostMapping("/staffs")
    public List<StaffResponse> staffs(@RequestBody ScheduleRequest scheduleRequest) {
        List<Flight> flights = scheduleRequest.flights();
        List<Staff> staffs = scheduleRequest.staffs();

        List<PassengerDistribution> distributions = passengerCalculator.estimateBy(flights);
        List<Counter> counters = counterScheduler.scheduleBy(distributions, flights);
        List<Staff> staff = staffScheduler.scheduleBy(counters, staffs);

        return staff.stream()
                .map(StaffResponse::new)
                .collect(Collectors.toList());
    }
}
