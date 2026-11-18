package uspace.domain.cruise;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.domain.cruise.booking.Booking;
import uspace.domain.cruise.booking.BookingId;
import uspace.domain.cruise.booking.Bookings;
import uspace.domain.cruise.booking.traveler.TravelerId;
import uspace.domain.cruise.cabin.CabinAvailabilities;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.itinerary.Itinerary;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;
import uspace.domain.cruise.zeroGravityExperience.exceptions.ZeroGravityExperienceBookingTimeException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CruiseTest {

    @Mock private Itinerary itineraryMock;
    @Mock private CabinAvailabilities cabinAvailabilitiesMock;
    @Mock private Bookings bookingsMock;
    @Mock private ZeroGravityExperience zeroGravityExperienceMock;
    @Mock private Booking bookingMock;

    @Test
    void givenBookingDateAfterCruiseDeparture_whenBookZeroG_thenThrowsZeroGravityExperienceBookingTimeException() {
        LocalDateTime departure = LocalDateTime.of(2030, 1, 5, 10, 0);
        LocalDateTime start     = LocalDateTime.of(2030, 1, 1, 10, 0);
        Cruise cruise = new Cruise(
                new CruiseId("CRUISE-1"),
                new CruiseDateTime(departure),
                new CruiseDateTime(start),
                cabinAvailabilitiesMock,
                bookingsMock,
                zeroGravityExperienceMock,
                itineraryMock
        );
        BookingId bookingId   = new BookingId("BOOK-1");
        TravelerId travelerId = new TravelerId("TRAVELER-1");
        CruiseDateTime bookingAfterDeparture = new CruiseDateTime(departure.plusHours(1));

        Runnable attempt = () -> cruise.bookZeroGravityExperience(bookingId, travelerId, bookingAfterDeparture);

        assertThrows(ZeroGravityExperienceBookingTimeException.class, attempt::run);
    }

    @Test
    void givenBookingDateBeforeOrEqualCruiseDeparture_whenBookZeroG_thenSucceeds() {
        LocalDateTime departure = LocalDateTime.of(2030, 1, 5, 10, 0);
        LocalDateTime start     = LocalDateTime.of(2030, 1, 1, 10, 0);
        Cruise cruise = new Cruise(
                new CruiseId("CRUISE-2"),
                new CruiseDateTime(departure),
                new CruiseDateTime(start),
                cabinAvailabilitiesMock,
                bookingsMock,
                zeroGravityExperienceMock,
                itineraryMock
        );
        BookingId bookingId   = new BookingId("BOOK-2");
        TravelerId travelerId = new TravelerId("TRAVELER-2");
        CruiseDateTime bookingAtDeparture = new CruiseDateTime(departure);

        when(bookingsMock.findById(bookingId)).thenReturn(bookingMock);

        Runnable attempt = () -> cruise.bookZeroGravityExperience(bookingId, travelerId, bookingAtDeparture);

        assertDoesNotThrow(attempt::run);
    }

    @Test
    void givenValidBookingDate_whenBookZeroG_thenDelegatesToBooking() {
        LocalDateTime departure = LocalDateTime.of(2030, 1, 5, 10, 0);
        LocalDateTime start     = LocalDateTime.of(2030, 1, 1, 10, 0);
        Cruise cruise = new Cruise(
                new CruiseId("CRUISE-3"),
                new CruiseDateTime(departure),
                new CruiseDateTime(start),
                cabinAvailabilitiesMock,
                bookingsMock,
                zeroGravityExperienceMock,
                itineraryMock
        );
        BookingId bookingId   = new BookingId("BOOK-3");
        TravelerId travelerId = new TravelerId("TRAVELER-3");
        CruiseDateTime bookingBeforeDeparture = new CruiseDateTime(departure.minusDays(1));

        when(bookingsMock.findById(bookingId)).thenReturn(bookingMock);

        cruise.bookZeroGravityExperience(bookingId, travelerId, bookingBeforeDeparture);

        verify(bookingMock, times(1))
                .bookZeroGravityExperience(travelerId, zeroGravityExperienceMock);
    }
}
