package com.caacetc.scheduling.plan.domain.counter;

import com.caacetc.scheduling.plan.domain.flight.Flight;
import com.caacetc.scheduling.plan.domain.flight.FlightDateTime;
import com.caacetc.scheduling.plan.domain.passenger.Distribution;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
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
    public List<Counter> scheduleBy(List<Distribution> distributions, List<Flight> flights) {
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
        counters.forEach(counter -> {
            try {
                for (FlightDateTime flightDateTime : flightDateTimes) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Calendar startTime = parseTime(flightDateTime.day() + " " + counter.openStartTime(), sdf);
                    Calendar endTime = computeEndTime(counter, flightDateTime, sdf);
                    OpenPeriod openPeriod = new OpenPeriod(counter.code(), startTime, endTime);

                    counter.openPeriods().add(openPeriod);
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        });
        return counters;
    }

    private Calendar parseTime(String startTime, SimpleDateFormat sdf) throws ParseException {
        Date start1 = sdf.parse(startTime);
        Calendar start2 = Calendar.getInstance();
        start2.setTime(start1);
        return start2;
    }

    private Calendar computeEndTime(Counter counter, FlightDateTime flightDateTime, SimpleDateFormat sdf) throws ParseException {
        String endTime;
        String domEndTime = flightDateTime.domEndTime();
        String intEndTime = flightDateTime.intEndTime();
        if (counter.isDomesticAndInternational()) {

            endTime = flightDateTime.day() + " " +
                    Optional.ofNullable(counter.openEndTime())
                            .orElse(domEndTime.compareTo(intEndTime) > 0 ? domEndTime : intEndTime);
        } else if (counter.isDomestic()) {
            endTime = flightDateTime.day() + " " + Optional.ofNullable(counter.openEndTime())
                    .orElse(domEndTime);
        } else {
            endTime = flightDateTime.day() + " " + Optional.ofNullable(counter.openEndTime())
                    .orElse(intEndTime);
        }

        return parseTime(endTime, sdf);
    }

    private List<FlightDateTime> dateTimeOf(List<Flight> flights) {
        List<FlightDateTime> flightDateTimes = new ArrayList<>();

        flights.forEach(flight -> {
            String day = new SimpleDateFormat("yyyy-MM-dd").format(flight.departTime());

            AtomicBoolean existDay = new AtomicBoolean(false);
            flightDateTimes.stream()
                    .filter(flightDateTime -> flightDateTime.day().equals(day))
                    .findFirst()
                    .ifPresent(flightDateTime -> {
                        existDay.set(true);

                        if (flight.isDom()) {
                            flightDateTime.setDomEndTime(flight.departTime());
                        } else {
                            flightDateTime.setIntEndTime(flight.departTime());
                        }
                    });
            if (!existDay.get()) {
                FlightDateTime dateTime = new FlightDateTime(day);
                if (flight.isDom()) {
                    dateTime.setDomEndTime(flight.departTime());
                } else {
                    dateTime.setIntEndTime(flight.departTime());
                }
                flightDateTimes.add(dateTime);
            }
        });

        return flightDateTimes;
    }
}
