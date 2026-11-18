package uspace.application.cruise.cabin;

import org.junit.jupiter.api.Test;
import uspace.domain.cruise.booking.BookingId;
import uspace.domain.cruise.cabin.CabinType;
import uspace.domain.cruise.dateTime.CruiseDateTime;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CabinAttributionEngineTest {

    private static final CruiseDateTime EARLIEST_BOOKING_DATE_TIME =
            new CruiseDateTime(LocalDateTime.of(2085, 1, 1, 10, 0));
    private static final CruiseDateTime MIDDLE_BOOKING_DATE_TIME =
            new CruiseDateTime(LocalDateTime.of(2085, 1, 1, 11, 0));
    private static final CruiseDateTime LATEST_BOOKING_DATE_TIME =
            new CruiseDateTime(LocalDateTime.of(2085, 1, 1, 12, 0));
    private static final String FIRST_STANDARD_CABIN_ID = "A1";
    private static final String SECOND_STANDARD_CABIN_ID = "A2";
    private static final String THIRD_STANDARD_CABIN_ID = "A3";
    private static final String FOURTH_STANDARD_CABIN_ID = "A4";

    private final CabinAttributionEngine cabinAttributionEngine = new CabinAttributionEngine();

    @Test
    void givenStandardBookings_whenAssignCabinsWithBookingDateTimeCriteria_thenCabinsAreAssignedByBookingDateTime() {
        CabinAttributionBooking secondBooking =
                new CabinAttributionBooking(new BookingId(), CabinType.STANDARD, 1, LATEST_BOOKING_DATE_TIME);
        CabinAttributionBooking firstBooking =
                new CabinAttributionBooking(new BookingId(), CabinType.STANDARD, 1, EARLIEST_BOOKING_DATE_TIME);

        List<CabinAttributionBooking> bookings = List.of(secondBooking, firstBooking);
        Map<CabinType, List<String>> cabinIdsByType =
                Map.of(CabinType.STANDARD, List.of(FIRST_STANDARD_CABIN_ID, SECOND_STANDARD_CABIN_ID));

        List<CabinAttributionResult> results =
                cabinAttributionEngine.assignCabins(bookings,
                        cabinIdsByType,
                        CabinAttributionCriteria.BOOKING_DATE_TIME);

        List<String> actualCabinAndBookingOrder = results.stream()
                .map(result -> result.getCabinId() + "-" + result.getBookingId())
                .toList();
        List<String> expectedCabinAndBookingOrder = List.of(
                FIRST_STANDARD_CABIN_ID + "-" + firstBooking.getBookingId().toString(),
                SECOND_STANDARD_CABIN_ID + "-" + secondBooking.getBookingId().toString()
        );

        assertEquals(expectedCabinAndBookingOrder, actualCabinAndBookingOrder);
    }

    @Test
    void givenStandardBookings_whenAssignCabinsWithTravelersCriteria_thenCabinsAreAssignedByTravelerCountThenBookingDateTime() {
        CabinAttributionBooking oneTravelerBooking =
                new CabinAttributionBooking(new BookingId(), CabinType.STANDARD, 1, EARLIEST_BOOKING_DATE_TIME);
        CabinAttributionBooking twoTravelersEarlierBooking =
                new CabinAttributionBooking(new BookingId(), CabinType.STANDARD, 2, MIDDLE_BOOKING_DATE_TIME);
        CabinAttributionBooking twoTravelersLaterBooking =
                new CabinAttributionBooking(new BookingId(), CabinType.STANDARD, 2, LATEST_BOOKING_DATE_TIME);
        CabinAttributionBooking threeTravelersBooking =
                new CabinAttributionBooking(new BookingId(), CabinType.STANDARD, 3, MIDDLE_BOOKING_DATE_TIME);

        List<CabinAttributionBooking> bookings = List.of(
                twoTravelersLaterBooking,
                oneTravelerBooking,
                threeTravelersBooking,
                twoTravelersEarlierBooking
        );
        Map<CabinType, List<String>> cabinIdsByType =
                Map.of(CabinType.STANDARD, List.of(FIRST_STANDARD_CABIN_ID,
                        SECOND_STANDARD_CABIN_ID,
                        THIRD_STANDARD_CABIN_ID,
                        FOURTH_STANDARD_CABIN_ID));

        List<CabinAttributionResult> results =
                cabinAttributionEngine.assignCabins(bookings,
                        cabinIdsByType,
                        CabinAttributionCriteria.TRAVELERS);

        List<String> actualBookingOrder = results.stream()
                .map(CabinAttributionResult::getBookingId)
                .toList();
        List<String> expectedBookingOrder = List.of(
                threeTravelersBooking.getBookingId().toString(),
                twoTravelersEarlierBooking.getBookingId().toString(),
                twoTravelersLaterBooking.getBookingId().toString(),
                oneTravelerBooking.getBookingId().toString()
        );

        assertEquals(expectedBookingOrder, actualBookingOrder);
    }
}
