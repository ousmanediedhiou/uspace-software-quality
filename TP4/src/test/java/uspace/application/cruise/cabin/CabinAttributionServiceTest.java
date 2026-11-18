package uspace.application.cruise.cabin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.application.cruise.cabin.dtos.CabinAttributionsDto;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.booking.Booking;
import uspace.domain.cruise.cabin.CabinConfiguration;
import uspace.domain.cruise.cabin.CabinType;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CabinAttributionServiceTest {

    private static final String ANY_CRUISE_ID_STR = "ANY_CRUISE_ID";
    private static final CruiseId ANY_CRUISE_ID = new CruiseId(ANY_CRUISE_ID_STR);
    private static final String ANY_CRITERIA_STRING = "bookingDateTime";
    private static final CabinAttributionsDto ANY_CABIN_ATTRIBUTIONS_DTO =
            new CabinAttributionsDto(List.of());

    private CabinAttributionService cabinAttributionService;

    @Mock
    private CruiseRepository cruiseRepositoryMock;
    @Mock
    private CabinConfiguration cabinConfigurationMock;
    @Mock
    private CabinAttributionEngine cabinAttributionEngineMock;
    @Mock
    private CabinAttributionAssembler cabinAttributionAssemblerMock;
    @Mock
    private Cruise cruiseMock;
    @Mock
    private Booking bookingMock;

    @BeforeEach
    void createCabinAttributionService() {
        cabinAttributionService = new CabinAttributionService(
                cruiseRepositoryMock,
                cabinConfigurationMock,
                cabinAttributionEngineMock,
                cabinAttributionAssemblerMock);
    }

    @Test
    void givenNonExistingCruise_whenGetCabinAttributions_thenThrowCruiseNotFoundException() {
        when(cruiseRepositoryMock.findById(ANY_CRUISE_ID)).thenReturn(null);

        Executable getCabinAttributions = () ->
                cabinAttributionService.getCabinAttributions(ANY_CRUISE_ID_STR, ANY_CRITERIA_STRING);

        assertThrows(CruiseNotFoundException.class, getCabinAttributions);
    }

    @Test
    void givenExistingCruise_whenGetCabinAttributions_thenReturnCabinAttributionsDto() {
        givenExistingCruise();
        givenBookingsForCruise();
        givenCabinConfiguration();
        givenCabinAttributionEngineResult();
        givenCabinAttributionAssemblerResult();

        CabinAttributionsDto cabinAttributionsDto =
                cabinAttributionService.getCabinAttributions(ANY_CRUISE_ID_STR, ANY_CRITERIA_STRING);

        assertSame(ANY_CABIN_ATTRIBUTIONS_DTO, cabinAttributionsDto);
    }

    private void givenExistingCruise() {
        when(cruiseRepositoryMock.findById(ANY_CRUISE_ID)).thenReturn(cruiseMock);
    }

    private void givenBookingsForCruise() {
        when(cruiseMock.getBookings()).thenReturn(List.of(bookingMock));
        when(bookingMock.getCabinType()).thenReturn(CabinType.STANDARD);
        when(bookingMock.getTravelers()).thenReturn(List.of());
        when(bookingMock.getBookingDateTime()).thenReturn(null);
    }

    private void givenCabinConfiguration() {
        when(cabinConfigurationMock.findCabinIdsByType())
                .thenReturn(Map.of(CabinType.STANDARD, List.of("A1")));
    }

    private void givenCabinAttributionEngineResult() {
        when(cabinAttributionEngineMock.assignCabins(any(), any(), any()))
                .thenReturn(List.of());
    }

    private void givenCabinAttributionAssemblerResult() {
        when(cabinAttributionAssemblerMock.toDto(any()))
                .thenReturn(ANY_CABIN_ATTRIBUTIONS_DTO);
    }
}
