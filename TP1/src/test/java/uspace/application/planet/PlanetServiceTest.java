package uspace.application.planet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.InOrder;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.application.cruise.exceptions.CruiseNotFoundException;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.itinerary.exceptions.InvalidPlanetDateException;
import uspace.domain.cruise.itinerary.exceptions.InvalidPlanetException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlanetServiceTest {

    @InjectMocks
    private PlanetService service;

    @Mock private CruiseRepository cruiseRepository;
    @Mock private PlanetValidator planetValidator;
    @Mock private Cruise cruise;

    private final LocalDateTime arrival = LocalDateTime.parse("2085-06-10T09:00");
    private final LocalDateTime departureShort = LocalDateTime.parse("2085-06-11T10:00");
    private final LocalDateTime departureValid = LocalDateTime.parse("2085-06-14T10:00");
    private final LocalDateTime cruiseStart = LocalDateTime.parse("2085-06-01T08:00");
    private final LocalDateTime cruiseEnd = LocalDateTime.parse("2085-06-30T20:00");


    @Test
    void givenDurationLessThan72Hours_whenAddPlanet_thenThrowsInvalidPlanetDateException() {
        var dto = new NewPlanetDto("IO", arrival, departureShort);

        assertThrows(InvalidPlanetDateException.class, () -> service.addPlanet("CRUISE-1", dto));

        verifyNoInteractions(planetValidator, cruiseRepository);
    }

    @Test
    void givenValidatorRejects_whenAddPlanet_thenThrowsInvalidPlanetException() {
        var dto = new NewPlanetDto("IO", arrival, departureValid);
        when(planetValidator.isValid("IO")).thenReturn(false);

        assertThrows(InvalidPlanetException.class, () -> service.addPlanet("CRUISE-1", dto));

        verify(planetValidator, times(1)).isValid("IO");
        verifyNoInteractions(cruiseRepository);
    }

    @Test
    void givenUnknownCruise_whenAddPlanet_thenThrowsCruiseNotFoundException() {
        var dto = new NewPlanetDto("EUROPA", arrival, departureValid);
        when(planetValidator.isValid("EUROPA")).thenReturn(true);
        when(cruiseRepository.findById(new CruiseId("UNKNOWN"))).thenReturn(null);

        assertThrows(CruiseNotFoundException.class, () -> service.addPlanet("UNKNOWN", dto));
    }

    @Test
    void givenValidPlanet_whenAddPlanet_thenDelegatesToDomainMethods() {
        var dto = new NewPlanetDto("EUROPA", arrival, departureValid);
        when(planetValidator.isValid("EUROPA")).thenReturn(true);
        when(cruiseRepository.findById(new CruiseId("CRUISE-OK"))).thenReturn(cruise);
        when(cruise.getDepartureDateTime()).thenReturn(new CruiseDateTime(cruiseStart));
        when(cruise.getEndDateTime()).thenReturn(new CruiseDateTime(cruiseEnd));
        when(cruise.getPlanetsInItinerary()).thenReturn(new ArrayList<>());

        service.addPlanet("CRUISE-OK", dto);

        InOrder order = inOrder(planetValidator, cruiseRepository, cruise);
        order.verify(planetValidator).isValid("EUROPA");
        order.verify(cruiseRepository).findById(new CruiseId("CRUISE-OK"));
        order.verify(cruise).getDepartureDateTime();
        order.verify(cruise).getEndDateTime();
        order.verify(cruise).getPlanetsInItinerary();
    }

    @Test
    void givenValidPlanet_whenAddPlanet_thenAddsPlanetToInternalList() {
        var dto = new NewPlanetDto("EUROPA", arrival, departureValid);
        when(planetValidator.isValid("EUROPA")).thenReturn(true);
        when(cruiseRepository.findById(new CruiseId("CRUISE-OK"))).thenReturn(cruise);
        when(cruise.getDepartureDateTime()).thenReturn(new CruiseDateTime(cruiseStart));
        when(cruise.getEndDateTime()).thenReturn(new CruiseDateTime(cruiseEnd));

        List<uspace.domain.cruise.itinerary.planet.Planet> planets = new ArrayList<>();
        when(cruise.getPlanetsInItinerary()).thenReturn(planets);

        service.addPlanet("CRUISE-OK", dto);

        assertEquals(1, planets.size());
    }

    @Test
    void givenExactly72Hours_whenAddPlanet_thenDoesNotThrow() {
        var dto = new NewPlanetDto("IO",
                LocalDateTime.parse("2085-06-10T09:00"),
                LocalDateTime.parse("2085-06-13T09:00"));
        when(planetValidator.isValid("IO")).thenReturn(true);
        when(cruiseRepository.findById(new CruiseId("CRUISE-OK"))).thenReturn(cruise);
        when(cruise.getDepartureDateTime()).thenReturn(new CruiseDateTime(cruiseStart));
        when(cruise.getEndDateTime()).thenReturn(new CruiseDateTime(cruiseEnd));
        when(cruise.getPlanetsInItinerary()).thenReturn(new ArrayList<>());

        assertDoesNotThrow(() -> service.addPlanet("CRUISE-OK", dto));
    }
}
