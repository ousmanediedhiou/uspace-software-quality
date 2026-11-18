package uspace.domain.cruise.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.domain.cruise.booking.traveler.Traveler;
import uspace.domain.cruise.booking.traveler.TravelerId;
import uspace.domain.cruise.booking.traveler.exceptions.TravelerNotFoundException;
import uspace.domain.cruise.cabin.CabinType;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingZeroGTest {

    @Mock private BookingId bookingId;
    @Mock private List<Traveler> travelers;           // List mockée inj.
    private final CabinType cabinType = CabinType.STANDARD; // enum = donnée inerte
    @Mock private CruiseDateTime bookingDateTime;

    @Mock private Traveler travelerMock;
    @Mock private TravelerId travelerIdPresent;
    @Mock private TravelerId travelerIdAbsent;
    @Mock private ZeroGravityExperience zeroGravityExperience;

    @InjectMocks
    private Booking booking;

    @BeforeEach
    void setUp() {
        when(travelerMock.getId()).thenReturn(travelerIdPresent);

        when(travelers.stream()).thenReturn(Stream.of(travelerMock));
    }

    @Test
    void givenTravelerInBooking_whenBookZeroG_thenDelegatesToTraveler() {
        booking.bookZeroGravityExperience(travelerIdPresent, zeroGravityExperience);

        verify(travelerMock, times(1)).bookZeroGravityExperience(zeroGravityExperience);
        verifyNoMoreInteractions(travelerMock);
        verifyNoInteractions(zeroGravityExperience);
    }

    @Test
    void givenTravelerNotInBooking_whenBookZeroG_thenThrowsTravelerNotFoundException() {
        when(travelerMock.getId()).thenReturn(travelerIdPresent);
        when(travelers.stream()).thenReturn(Stream.of(travelerMock));

        assertThrows(TravelerNotFoundException.class,
                () -> booking.bookZeroGravityExperience(travelerIdAbsent, zeroGravityExperience));

        verify(travelerMock, never()).bookZeroGravityExperience(any(ZeroGravityExperience.class));
        verifyNoInteractions(zeroGravityExperience);
    }
}
