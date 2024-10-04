package com.gridnine.testing;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;





class FlightFilterChain implements FlightFilter {
    private List<FlightFilter> filters = new ArrayList<>();

    public FlightFilterChain addFilter(FlightFilter filter) {
        filters.add(filter);
        return this;
    }

    @Override
    public List<Flight> filter(List<Flight> flights) {
        List<Flight> result = flights;
        for (FlightFilter filter : filters) {
            result = filter.filter(result);
        }
        return result;
    }
    public void clearFilters() {
        filters.clear();
    }
}

interface FlightFilter {
    List<Flight> filter(List<Flight> flights);
}

class Filter {

    private final static DateTimeFormatter fmt =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

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


class DepartureBeforeNow extends Filter implements FlightFilter {

    public List<Flight> filter(List<Flight> flights) {
        LocalDateTime dt = LocalDateTime.now();
        List<Flight> filteredFlights = new ArrayList<>();
        for (Flight flight : flights) {
            List<LocalDateTime> dates = getDates(flight);
            LocalDateTime departureTime = dates.get(0);
            if (!departureTime.isBefore(dt)) {
                filteredFlights.add(flight);
            }
        }
        return filteredFlights;
    }
}




class SegmentsWithDepartureBeforeArrival extends Filter implements FlightFilter {
    public List<Flight> filter(List<Flight> flights) {
        boolean wrongSegemts = false;
        List<Flight> filteredFlights = new ArrayList<>();
        for (Flight flight : flights) {
            List<LocalDateTime> dates = getDates(flight);
            for (int i = 0; i < dates.size(); i += 2) {
                if (dates.get(i).isAfter(dates.get(i + 1))) {
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
}

class SegmentsWithOnLandTimeMoreThan2 extends Filter implements FlightFilter {
    public List<Flight> filter(List<Flight> flights) {
        List<Flight> filteredFlights = new ArrayList<>();
        Duration onLandTime = Duration.ZERO;
        for (Flight flight : flights) {
            List<Segment> segments = flight.getSegments();
            if (segments.size() > 1) {
                List<LocalDateTime> dates = getDates(flight);
                for (int i = 3; i < dates.size(); i += 2) {
                    onLandTime = onLandTime.plus(Duration.between(dates.get(i - 1), dates.get(i)));

                }
                System.out.println(onLandTime.toHours());
                if (onLandTime.toHours() <= 2) {
                    filteredFlights.add(flight);
                }
            } else {
                filteredFlights.add(flight);
            }
        }
        return filteredFlights;
    }
    }



