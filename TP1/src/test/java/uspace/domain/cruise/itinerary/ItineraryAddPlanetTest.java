package uspace.domain.cruise.itinerary;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.itinerary.exceptions.InvalidPlanetDateException;
import uspace.domain.cruise.itinerary.exceptions.PlanetAlreadyExistsException;
import uspace.domain.cruise.itinerary.planet.Planet;
import uspace.domain.cruise.itinerary.planet.PlanetName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItineraryAddPlanetTest {

    @InjectMocks
    private Itinerary itinerary;

    @Mock private List<Planet> planets;
    @Mock private Planet newPlanet;
    @Mock private Planet existingPlanet;
    @Mock private PlanetName planetName;
    @Mock private CruiseDateTime arrival;
    @Mock private CruiseDateTime departure;
    @Mock private CruiseDateTime cruiseStart;
    @Mock private CruiseDateTime cruiseEnd;

    @Test
    void givenDuplicateName_whenAddPlanet_thenThrowPlanetAlreadyExists() {
        when(planets.stream()).thenReturn(java.util.stream.Stream.of(existingPlanet));
        when(existingPlanet.getName()).thenReturn(planetName);
        when(newPlanet.getName()).thenReturn(planetName);

        assertThrows(PlanetAlreadyExistsException.class,
                () -> itinerary.addPlanet(newPlanet, cruiseStart, cruiseEnd));
    }

    @Test
    void givenArrivalBeforeCruiseStart_whenAddPlanet_thenThrowInvalidPlanetDate() {
        when(planets.stream()).thenReturn(java.util.stream.Stream.empty());
        when(newPlanet.getArrivalDate()).thenReturn(arrival);
        when(newPlanet.getDepartureDate()).thenReturn(departure);
        when(arrival.isAfter(cruiseStart)).thenReturn(false);

        assertThrows(InvalidPlanetDateException.class,
                () -> itinerary.addPlanet(newPlanet, cruiseStart, cruiseEnd));
    }

    @Test
    void givenDepartureAfterCruiseEnd_whenAddPlanet_thenThrowInvalidPlanetDate() {
        when(planets.stream()).thenReturn(java.util.stream.Stream.empty());
        when(newPlanet.getArrivalDate()).thenReturn(arrival);
        when(newPlanet.getDepartureDate()).thenReturn(departure);
        when(arrival.isAfter(cruiseStart)).thenReturn(true);
        when(departure.isBefore(cruiseEnd)).thenReturn(false);

        assertThrows(InvalidPlanetDateException.class,
                () -> itinerary.addPlanet(newPlanet, cruiseStart, cruiseEnd));
    }

    @Test
    void givenDepartureBeforeArrival_whenAddPlanet_thenThrowInvalidPlanetDate() {
        when(planets.stream()).thenReturn(java.util.stream.Stream.empty());
        when(newPlanet.getArrivalDate()).thenReturn(arrival);
        when(newPlanet.getDepartureDate()).thenReturn(departure);
        when(arrival.isAfter(cruiseStart)).thenReturn(true);
        when(departure.isBefore(cruiseEnd)).thenReturn(true);
        when(departure.isAfter(arrival)).thenReturn(false);

        assertThrows(InvalidPlanetDateException.class,
                () -> itinerary.addPlanet(newPlanet, cruiseStart, cruiseEnd));
    }

    @Test
    void givenOverlappingInterval_whenAddPlanet_thenThrowInvalidPlanetDate() {
        when(planets.stream()).thenReturn(java.util.stream.Stream.empty());
        when(planets.iterator()).thenReturn(List.of(existingPlanet).iterator());
        when(newPlanet.getArrivalDate()).thenReturn(arrival);
        when(newPlanet.getDepartureDate()).thenReturn(departure);
        when(arrival.isAfter(cruiseStart)).thenReturn(true);
        when(departure.isBefore(cruiseEnd)).thenReturn(true);
        when(departure.isAfter(arrival)).thenReturn(true);
        when(existingPlanet.getArrivalDate()).thenReturn(arrival);
        when(existingPlanet.getDepartureDate()).thenReturn(departure);

        assertThrows(InvalidPlanetDateException.class,
                () -> itinerary.addPlanet(newPlanet, cruiseStart, cruiseEnd));
    }

    @Test
    void givenValidWindowAndUniqueName_whenAddPlanet_thenAddsPlanet() {
        when(planets.stream()).thenReturn(java.util.stream.Stream.empty());
        when(planets.iterator()).thenReturn(List.<Planet>of().iterator());
        when(newPlanet.getArrivalDate()).thenReturn(arrival);
        when(newPlanet.getDepartureDate()).thenReturn(departure);
        when(arrival.isAfter(cruiseStart)).thenReturn(true);
        when(departure.isBefore(cruiseEnd)).thenReturn(true);
        when(departure.isAfter(arrival)).thenReturn(true);

        itinerary.addPlanet(newPlanet, cruiseStart, cruiseEnd);

        verify(planets, times(1)).add(newPlanet);
    }
}
