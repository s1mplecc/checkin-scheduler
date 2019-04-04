package com.caacetc.scheduling.plan.domain.counter;

import com.caacetc.scheduling.plan.domain.flight.Flight;
import com.caacetc.scheduling.plan.domain.flight.FlightDateTime;
import com.caacetc.scheduling.plan.domain.passenger.Distribution;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
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
    public List<Counter> scheduleBy(final List<Distribution> passengerDistributions, List<Flight> flights) {
        List<CounterDistribution> counterDistributions = reduce(passengerDistributions);

        List<Counter> result = new ArrayList<>();
        result.addAll(scheduleMustOpenCounters(dateTimeOf(flights)));
        result.addAll(scheduleOnDemandOpenCounters(counterDistributions, passengerDistributions));

        return result.stream()
                .filter(counter -> !counter.openPeriods().isEmpty())
                .sorted()
                .collect(Collectors.toList());
    }

    private List<Counter> scheduleOnDemandOpenCounters(List<CounterDistribution> counterDistributions, List<Distribution> distributions) {
        List<Counter> counters = new ArrayList<>();

        List<Counter> onDemandPremiumCounters = repository.onDemandPremiumCounters();
        List<Counter> onDemandIntEconomyCounters = repository.onDemandIntEconomyCounters();
        List<Counter> onDemandDomEconomyCounters = repository.onDemandDomEconomyCounters();

        counterDistributions.forEach(distribution -> scheduleBy(distribution, onDemandPremiumCounters, onDemandDomEconomyCounters, onDemandIntEconomyCounters));

        counters.addAll(onDemandDomEconomyCounters);
        counters.addAll(onDemandIntEconomyCounters);
        counters.addAll(onDemandPremiumCounters);
        return counters;
    }

    private void scheduleBy(CounterDistribution distribution, List<Counter> onDemandPremiumCounters, List<Counter> onDemandDomEconomyCounters, List<Counter> onDemandIntEconomyCounters) {
        int temp1 = Math.min(onDemandDomEconomyCounters.size(), distribution.domEconomyNum());
        for (int i = 0; i < temp1; i++) {
            onDemandDomEconomyCounters.get(i).open(distribution.startTime(), distribution.endTime());
        }
        int temp2 = Math.min(onDemandIntEconomyCounters.size(), distribution.intEconomyNum());
        for (int i = 0; i < temp2; i++) {
            onDemandIntEconomyCounters.get(i).open(distribution.startTime(), distribution.endTime());
        }
        int temp3 = Math.min(onDemandPremiumCounters.size(), distribution.premiumNum());
        for (int i = 0; i < temp3; i++) {
            onDemandPremiumCounters.get(i).open(distribution.startTime(), distribution.endTime());
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
        DateTimeFormatter startTimeFormatter = DateTimeFormatter.ofPattern("H:mm");
        DateTimeFormatter endTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        List<Counter> counters = repository.mustOpenCounters();
        counters.stream()
                .filter(Counter::isDom)
                .forEach(counterConsumer(flightDateTimes, startTimeFormatter, endTimeFormatter, true));
        counters.stream()
                .filter(Counter::isInt)
                .forEach(counterConsumer(flightDateTimes, startTimeFormatter, endTimeFormatter, false));
        return counters;
    }

    private Consumer<Counter> counterConsumer(List<FlightDateTime> flightDateTimes, DateTimeFormatter startTimeFormatter, DateTimeFormatter endTimeFormatter, boolean isDom) {
        return counter -> {
            for (FlightDateTime flightDateTime : flightDateTimes) {
                LocalDateTime startTime = LocalDateTime.of(
                        flightDateTime.date(),
                        LocalTime.from(startTimeFormatter.parse(counter.openStartTime())));

                LocalDateTime endTime = Optional.ofNullable(counter.openEndTime())
                        .map(t -> LocalDateTime.of(
                                flightDateTime.date(),
                                LocalTime.from(endTimeFormatter.parse(counter.openEndTime()))))
                        .orElse(isDom
                                ? LocalDateTime.of(flightDateTime.date(), flightDateTime.domEndTime())
                                : LocalDateTime.of(flightDateTime.date(), flightDateTime.intEndTime()));

                List<OpenFragment> openFragments = new OpenFragment(counter.code(), startTime, endTime).splitBy3Hours();
                counter.openPeriods().addAll(openFragments);
            }
        };
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

            LocalTime domEndTime;
            if (domFlights.isEmpty()) {
                domEndTime = LocalTime.MAX;
            } else {
                domEndTime = domFlights.get(domFlights.size() - 1).departTime().toLocalTime();
            }

            List<Flight> intFlights = flights.stream()
                    .filter(flight -> flight.departTime().toLocalDate().isEqual(flightDateTime.date()))
                    .filter(flight -> flight.isInt() || flight.isMix())
                    .collect(Collectors.toList());

            LocalTime intEndTime;
            if (intFlights.isEmpty()) {
                intEndTime = LocalTime.MAX;
            } else {
                intEndTime = intFlights.get(intFlights.size() - 1).departTime().toLocalTime();
            }

            flightDateTime.setDomEndTime(domEndTime);
            flightDateTime.setIntEndTime(intEndTime);
        });

        return flightDateTimes;
    }
}
