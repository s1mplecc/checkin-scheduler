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
import java.util.stream.Stream;

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
        reduce(distributions);
        List<Counter> result = new ArrayList<>();

        List<Counter> mustOpenCounters = repository.mustOpenCounters();
        List<Counter> onDemandPremiumCounters = repository.onDemandPremiumCounters();
        List<Counter> onDemandIntEconomyCounters = repository.onDemandIntEconomyCounters();
        List<Counter> onDemandDomEconomyCounters = repository.onDemandDomEconomyCounters();

        scheduleMustOpenCounters(mustOpenCounters, dateTimeOf(flights));
        distributions.forEach(distribution -> {
            scheduleBy(distribution, onDemandPremiumCounters, distribution.premiumCounters());
            scheduleBy(distribution, onDemandDomEconomyCounters, distribution.domEconomyCounters());
            scheduleBy(distribution, onDemandIntEconomyCounters, distribution.intEconomyCounters() - 11); // 国际经济舱人工办理柜台必须开放个数
        });

        result.addAll(mustOpenCounters);
        result.addAll(onDemandDomEconomyCounters);
        result.addAll(onDemandIntEconomyCounters);
        result.addAll(onDemandPremiumCounters);

        return result.stream()
                .filter(counter -> !counter.openPeriods().isEmpty())
                .sorted()
                .collect(Collectors.toList());
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private List<CounterDistribution> reduce(final List<Distribution> distributions) {
        List<CounterDistribution> result = new ArrayList<>();

        for (int i = 0; i < distributions.size(); i++) {
            if (i % 12 == 0) {
                Stream<Distribution> stream;
                if (i + 12 >= distributions.size()) {
                    stream = distributions.subList(i, distributions.size()).stream();
                } else {
                    stream = distributions.subList(i, i + 12).stream();
                }
                int maxDomEco = stream.mapToInt(Distribution::domEconomyCounters).max().getAsInt();
                int maxIntEco = stream.mapToInt(Distribution::intEconomyCounters).max().getAsInt();
                int maxPre = stream.mapToInt(Distribution::premiumCounters).max().getAsInt();

                result.add(new CounterDistribution(distributions.get(i).instant(), maxDomEco, maxIntEco, maxPre));
            }
        }
        return result;
    }

    /**
     * @param distribution passenger distribution divided by 5 minutes
     * @param counters     to be scheduled
     * @param needs        number needs open counter
     */
    private void scheduleBy(Distribution distribution, List<Counter> counters, int needs) {
        int temp = Math.min(counters.size(), needs);
        for (int i = 0; i < temp; i++) {
            Calendar endTime = (Calendar) distribution.instant().clone();
            endTime.add(Calendar.MINUTE, 5);
            counters.get(i).open(distribution.instant(), endTime);
        }
    }

    private void scheduleMustOpenCounters(List<Counter> mustOpenCounters, List<FlightDateTime> flightDateTimes) {
        mustOpenCounters.forEach(counter -> {
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
            String day = new SimpleDateFormat("yyyy-MM-dd").format(flight.getDepartTime());

            AtomicBoolean existDay = new AtomicBoolean(false);
            flightDateTimes.stream()
                    .filter(flightDateTime -> flightDateTime.day().equals(day))
                    .findFirst()
                    .ifPresent(flightDateTime -> {
                        existDay.set(true);

                        if (flight.isDom()) {
                            flightDateTime.setDomEndTime(flight.getDepartTime());
                        } else {
                            flightDateTime.setIntEndTime(flight.getDepartTime());
                        }
                    });
            if (!existDay.get()) {
                FlightDateTime dateTime = new FlightDateTime(day);
                if (flight.isDom()) {
                    dateTime.setDomEndTime(flight.getDepartTime());
                } else {
                    dateTime.setIntEndTime(flight.getDepartTime());
                }
                flightDateTimes.add(dateTime);
            }
        });

        return flightDateTimes;
    }
}
