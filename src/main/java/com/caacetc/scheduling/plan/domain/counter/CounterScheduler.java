package com.caacetc.scheduling.plan.domain.counter;

import com.caacetc.scheduling.plan.domain.flight.Flight;
import com.caacetc.scheduling.plan.domain.flight.FlightDateTime;
import com.caacetc.scheduling.plan.domain.passenger.Distribution;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 柜台调度类，包括：
 * 1、必开柜台（开放时间段、柜台号），配置传入
 * 2、按需开放（计算所需柜台数大于必须开放数）
 */
@Service
public class CounterScheduler {
    @Resource
    private CounterRepository repository;

    public CounterScheduler() {
        repository = new CounterRepository();
    }

    /**
     * Compute each counter open periods
     */
    public List<Counter> scheduleBy(final List<Distribution> distributions, List<Flight> flights) {
        List<CounterDistribution> counterDistributions = reduce(distributions);

        List<Counter> result = new ArrayList<>();
        result.addAll(scheduleMustOpenCounters(dateTimeOf(flights)));

        List<Counter> onDemandPremiumCounters = repository.onDemandPremiumCounters();
        List<Counter> onDemandIntEconomyCounters = repository.onDemandIntEconomyCounters();
        List<Counter> onDemandDomEconomyCounters = repository.onDemandDomEconomyCounters();

        counterDistributions.forEach(distribution -> scheduleBy(distribution, onDemandPremiumCounters, onDemandDomEconomyCounters, onDemandIntEconomyCounters));

        result.addAll(onDemandDomEconomyCounters);
        result.addAll(onDemandIntEconomyCounters);
        result.addAll(onDemandPremiumCounters);

        return result.stream()
                .filter(counter -> !counter.openPeriods().isEmpty())
                .sorted()
                .collect(Collectors.toList());
    }

    private void scheduleBy(CounterDistribution distribution, List<Counter> onDemandPremiumCounters, List<Counter> onDemandDomEconomyCounters, List<Counter> onDemandIntEconomyCounters) {
        int temp1 = Math.min(onDemandDomEconomyCounters.size(), distribution.domEconomyNum());
        for (int i = 0; i < temp1; i++) {
            onDemandDomEconomyCounters.get(i).open(distribution.startTime());
        }
        int temp2 = Math.min(onDemandIntEconomyCounters.size(), distribution.intEconomyNum());
        for (int i = 0; i < temp2; i++) {
            onDemandIntEconomyCounters.get(i).open(distribution.startTime());
        }
        int temp3 = Math.min(onDemandPremiumCounters.size(), distribution.premiumNum());
        for (int i = 0; i < temp3; i++) {
            onDemandPremiumCounters.get(i).open(distribution.startTime());
        }
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private List<CounterDistribution> reduce(final List<Distribution> distributions) {
        List<CounterDistribution> counterDistributions = new ArrayList<>();

        for (int i = 0; i < distributions.size(); i++) {
            if (i % 12 == 0) {
                List<Distribution> subList;
                if (i + 12 >= distributions.size()) {
                    subList = distributions.subList(i, distributions.size());
                } else {
                    subList = distributions.subList(i, i + 12);
                }
                int maxDomEco = subList.stream().mapToInt(Distribution::domEconomyCounters).max().getAsInt();
                int maxIntEco = subList.stream().mapToInt(Distribution::intEconomyCounters).max().getAsInt();
                int maxPre = subList.stream().mapToInt(Distribution::premiumCounters).max().getAsInt();

                counterDistributions.add(new CounterDistribution(distributions.get(i).instant(), maxDomEco, maxIntEco, maxPre));
            }
        }
        return counterDistributions;
    }

    private List<Counter> scheduleMustOpenCounters(List<FlightDateTime> flightDateTimes) {
        List<Counter> counters = repository.mustOpenCounters();
        counters.stream()
                .filter(Counter::isDom)
                .forEach(counter -> {
                    for (FlightDateTime flightDateTime : flightDateTimes) {
                        // todo-zz
                        LocalDateTime startTime = LocalDateTime.of(flightDateTime.date(), LocalTime.parse("0" + counter.openStartTime() + ":00"));

                        LocalDateTime endTime = Optional.ofNullable(counter.openEndTime())
                                .map(t -> LocalDateTime.of(flightDateTime.date(), LocalTime.parse(t + ":00")))
                                .orElse(LocalDateTime.of(flightDateTime.date(), flightDateTime.domEndTime()));

                        OpenPeriod openPeriod = new OpenPeriod(counter.code(), startTime, endTime);
                        counter.openPeriods().add(openPeriod);
                    }
                });
        counters.stream()
                .filter(Counter::isInt)
                .forEach(counter -> {
                    for (FlightDateTime flightDateTime : flightDateTimes) {
                        // todo-zz
                        LocalDateTime startTime = LocalDateTime.of(flightDateTime.date(), LocalTime.parse("0" + counter.openStartTime() + ":00"));

                        LocalDateTime endTime = Optional.ofNullable(counter.openEndTime())
                                .map(t -> LocalDateTime.of(flightDateTime.date(), LocalTime.parse(t + ":00")))
                                .orElse(LocalDateTime.of(flightDateTime.date(), flightDateTime.intEndTime()));

                        OpenPeriod openPeriod = new OpenPeriod(counter.code(), startTime, endTime);
                        counter.openPeriods().add(openPeriod);
                    }
                });
        return counters;
    }

    private List<FlightDateTime> dateTimeOf(List<Flight> flights) {
        List<FlightDateTime> flightDateTimes = new ArrayList<>();

        LocalDate startDate = flights.get(0).departTime().toLocalDate();
        LocalDate endDate = flights.get(flights.size() - 1).departTime().toLocalDate();

        LocalDate flag = startDate;
        while (!flag.isAfter(endDate)) {
            flightDateTimes.add(new FlightDateTime(flag));
            flag = flag.plusDays(1);
        }

        flightDateTimes.forEach(flightDateTime -> {
            List<Flight> domFlights = flights.stream()
                    .filter(flight -> flight.departTime().toLocalDate().isEqual(flightDateTime.date()))
                    .filter(flight -> flight.isDom() || flight.isMix())
                    .collect(Collectors.toList());

            LocalTime domEndTime = domFlights.get(domFlights.size() - 1).departTime().toLocalTime();

            List<Flight> intFlights = flights.stream()
                    .filter(flight -> flight.departTime().toLocalDate().isEqual(flightDateTime.date()))
                    .filter(flight -> flight.isInt() || flight.isMix())
                    .collect(Collectors.toList());

            LocalTime intEndTime = domFlights.get(intFlights.size() - 1).departTime().toLocalTime();

            flightDateTime.setDomEndTime(domEndTime);
            flightDateTime.setIntEndTime(intEndTime);
        });

        return flightDateTimes;
    }
}
