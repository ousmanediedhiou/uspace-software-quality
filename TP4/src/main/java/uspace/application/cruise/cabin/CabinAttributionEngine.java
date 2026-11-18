package uspace.application.cruise.cabin;

import uspace.domain.cruise.cabin.CabinType;
import uspace.domain.cruise.dateTime.CruiseDateTime;

import java.util.*;
import java.util.stream.Collectors;

public class CabinAttributionEngine {

    public List<CabinAttributionResult> assignCabins(List<CabinAttributionBooking> bookings,
                                                     Map<CabinType, List<String>> cabinIdsByType,
                                                     CabinAttributionCriteria criteria) {

        Map<CabinType, List<CabinAttributionBooking>> bookingsByType = bookings.stream()
                .collect(Collectors.groupingBy(CabinAttributionBooking::getCabinType));

        List<CabinAttributionResult> results = new ArrayList<>();

        for (Map.Entry<CabinType, List<CabinAttributionBooking>> entry : bookingsByType.entrySet()) {
            CabinType cabinType = entry.getKey();
            List<CabinAttributionBooking> bookingsForType = new ArrayList<>(entry.getValue());

            Comparator<CabinAttributionBooking> comparator = createComparator(criteria);
            bookingsForType.sort(comparator);

            List<String> cabinIds = cabinIdsByType.getOrDefault(cabinType, List.of());

            int limit = Math.min(bookingsForType.size(), cabinIds.size());
            for (int index = 0; index < limit; index++) {
                CabinAttributionBooking booking = bookingsForType.get(index);
                String cabinId = cabinIds.get(index);

                results.add(new CabinAttributionResult(
                        cabinId,
                        booking.getBookingId().toString(),
                        cabinType));
            }
        }

        return results;
    }

    private Comparator<CabinAttributionBooking> createComparator(CabinAttributionCriteria criteria) {
        if (criteria == CabinAttributionCriteria.BOOKING_DATE_TIME) {
            return (first, second) -> compareCruiseDateTime(first.getBookingDateTime(), second.getBookingDateTime());
        }

        if (criteria == CabinAttributionCriteria.TRAVELERS) {
            return Comparator
                    .comparingInt(CabinAttributionBooking::getTravelerCount)
                    .reversed()
                    .thenComparing((first, second) -> compareCruiseDateTime(first.getBookingDateTime(), second.getBookingDateTime()));
        }

        return (first, second) -> compareCruiseDateTime(first.getBookingDateTime(), second.getBookingDateTime());
    }

    private int compareCruiseDateTime(CruiseDateTime first, CruiseDateTime second) {
        if (first.isBefore(second)) {
            return -1;
        }

        if (first.isAfter(second)) {
            return 1;
        }

        return 0;
    }
}
