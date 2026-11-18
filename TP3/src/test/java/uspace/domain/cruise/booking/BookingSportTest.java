package uspace.domain.cruise.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.domain.cruise.booking.invoice.Invoice;
import uspace.domain.cruise.booking.traveler.Traveler;
import uspace.domain.cruise.booking.traveler.TravelerId;
import uspace.domain.cruise.booking.traveler.exceptions.TravelerNotFoundException;
import uspace.domain.cruise.cabin.CabinType;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.sport.Sport;
import uspace.domain.cruise.sport.SportAudience;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingSportTest {

    private static final String ANY_BOOKING_ID_STR = "ANY_BOOKING_ID";
    private static final BookingId ANY_BOOKING_ID = new BookingId(ANY_BOOKING_ID_STR);
    private static final TravelerId EXISTING_TRAVELER_ID = new TravelerId();
    private static final TravelerId UNKNOWN_TRAVELER_ID = new TravelerId();
    private static final LocalDateTime ANY_BOOKING_LOCAL_DATE_TIME = LocalDateTime.of(2080, 1, 1, 12, 0);
    private static final CruiseDateTime ANY_BOOKING_DATE_TIME = new CruiseDateTime(ANY_BOOKING_LOCAL_DATE_TIME);
    private static final CabinType ANY_CABIN_TYPE = CabinType.STANDARD;
    private static final String ANY_SPORT_NAME = "ANY_SPORT";

    @Mock
    private Traveler travelerMock;
    @Mock
    private Invoice invoiceMock;

    @Test
    void givenBookingWithTraveler_whenBookSport_thenDelegateToTravelerBookSport() {
        when(travelerMock.getId()).thenReturn(EXISTING_TRAVELER_ID);
        Sport sport = new Sport(ANY_SPORT_NAME, SportAudience.ADULT);
        Booking booking = new Booking(
                ANY_BOOKING_ID,
                List.of(travelerMock),
                ANY_CABIN_TYPE,
                ANY_BOOKING_DATE_TIME,
                invoiceMock
        );

        booking.bookSport(EXISTING_TRAVELER_ID, sport);

        verify(travelerMock).bookSport(sport);
    }

    @Test
    void givenBookingWithoutTraveler_whenBookSport_thenThrowTravelerNotFoundException() {
        when(travelerMock.getId()).thenReturn(EXISTING_TRAVELER_ID);
        Sport sport = new Sport(ANY_SPORT_NAME, SportAudience.ADULT);
        Booking booking = new Booking(
                ANY_BOOKING_ID,
                List.of(travelerMock),
                ANY_CABIN_TYPE,
                ANY_BOOKING_DATE_TIME,
                invoiceMock
        );

        Executable bookSport = () -> booking.bookSport(UNKNOWN_TRAVELER_ID, sport);

        assertThrows(TravelerNotFoundException.class, bookSport);
    }
}
