package uspace.application.cruise;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.domain.cruise.*;
import uspace.domain.cruise.booking.BookingId;
import uspace.domain.cruise.booking.traveler.TravelerId;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ZeroGBookingServiceTest {

    @InjectMocks
    private ZeroGBookingService service;

    @Mock private CruiseRepository cruiseRepository;
    @Mock private Cruise cruise;

    @Mock private CruiseId cruiseId;
    @Mock private BookingId bookingId;
    @Mock private TravelerId travelerId;
    @Mock private CruiseDateTime experienceDateTime;

    @Test
    void givenCruiseNotFound_whenBookZeroG_thenThrowsCruiseNotFoundException() {
        when(cruiseRepository.findById(cruiseId)).thenReturn(null);

        assertThrows(CruiseNotFoundException.class,
                () -> service.bookZeroG(cruiseId, bookingId, travelerId, experienceDateTime));

        verify(cruiseRepository, times(1)).findById(cruiseId);
        verifyNoMoreInteractions(cruiseRepository);
        verifyNoInteractions(cruise);
    }

    @Test
    void givenCruiseFound_whenBookZeroG_thenDelegatesToCruise() {
        when(cruiseRepository.findById(cruiseId)).thenReturn(cruise);

        service.bookZeroG(cruiseId, bookingId, travelerId, experienceDateTime);

        verify(cruise, times(1))
                .bookZeroGravityExperience(bookingId, travelerId, experienceDateTime);

        verify(cruiseRepository, times(1)).findById(cruiseId);
        verifyNoMoreInteractions(cruiseRepository);
    }
}
