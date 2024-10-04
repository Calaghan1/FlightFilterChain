package com.gridnine.testing;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Filter {

    private final static DateTimeFormatter fmt =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    public List<Flight> DepartureBefore(List<Flight> flights, LocalDateTime dT) {
        List<Flight> filteredFlights = new ArrayList<>();
        for (Flight flight : flights) {
                List<LocalDateTime> dates = getDates(flight);
                LocalDateTime departureTime = dates.getFirst();
                if (!departureTime.isBefore(dT)) {
                    filteredFlights.add(flight);
                }
        }
        return filteredFlights;
    }

    public List<Flight> wrongSegemts(List<Flight> flights) {
        boolean wrongSegemts = false;
        List<Flight> filteredFlights = new ArrayList<>();
        for (Flight flight : flights) {
            List<LocalDateTime> dates = getDates(flight);
            for (int i = 0; i < dates.size(); i += 2) {
                if (dates.get(i).isAfter(dates.get(i + 1))){
                    wrongSegemts = true;
                    break;
                }
            }
            if (!wrongSegemts) {
                filteredFlights.add(flight);
            }
            wrongSegemts = false;
        }
        return filteredFlights;
    }

    public List<Flight> onLandTime(List<Flight> flights) {
        List<Flight> filteredFlights = new ArrayList<>();
        Duration onLandTime = Duration.ZERO;
        for (Flight flight : flights) {
            List<LocalDateTime> dates = getDates(flight);
            int landAndTakeOf = flight.getSegments().size() - 1;

            for (int i = 0; i < landAndTakeOf; i += 2) {
                    onLandTime = onLandTime.plus(Duration.between(dates.get(i), dates.get(i + 2)));
                }
                if (onLandTime.toHours() < 2) {
                    filteredFlights.add(flight);
                }
                onLandTime = Duration.ZERO;
            }
        return filteredFlights;
    }
    public List<LocalDateTime> getDates(Flight flight) {
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        List<LocalDateTime> dates = new ArrayList<>();
        List<Segment> segments = flight.getSegments();
        for (Segment segment : segments) {
            List<String> str = List.of(segment.toString().split("\\|"));
            LocalDateTime departureTime = LocalDateTime.parse(str.get(0).replace("[", ""), fmt);
            LocalDateTime arrivalTime = LocalDateTime.parse(str.get(1).replace("]", ""), fmt);
            dates.add(departureTime);
            dates.add(arrivalTime);
        }
        return dates;
    }
}
