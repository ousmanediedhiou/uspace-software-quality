package uspace.application.cruise.planet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.application.cruise.planet.dtos.PlanetDto;
import uspace.application.utils.dateTimeParser.LocalDateTimeParser;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;
import uspace.domain.cruise.itinerary.planet.Planet;
import uspace.domain.cruise.itinerary.planet.PlanetName;
import uspace.domain.cruise.itinerary.planet.PlanetValidator;
import uspace.domain.cruise.itinerary.planet.exceptions.InvalidPlanetException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlanetServiceTest {

    private static final String ANY_CRUISE_ID_STR = "ANY_CRUISE_ID";
    private static final CruiseId ANY_CRUISE_ID = new CruiseId(ANY_CRUISE_ID_STR);
    private static final String ANY_PLANET_NAME = "ANY_PLANET_NAME";
    private static final String ANY_ARRIVAL_DATE_TIME_STR = "2084-04-08T12:30";
    private static final String ANY_DEPARTURE_DATE_TIME_STR = "2084-06-08T13:30";
    private static final PlanetDto ANY_PLANET_DTO = new PlanetDto(ANY_PLANET_NAME, ANY_ARRIVAL_DATE_TIME_STR, ANY_DEPARTURE_DATE_TIME_STR);
    private static final LocalDateTime ANY_ARRIVAL_LOCAL_DATE_TIME = LocalDateTime.of(2084, 4, 8, 12, 30);
    private static final LocalDateTime ANY_DEPARTURE_LOCAL_DATE_TIME = LocalDateTime.of(2084, 6, 8, 13, 30);
    private static final CruiseDateTime ANY_ARRIVAL_DATE_TIME = new CruiseDateTime(ANY_ARRIVAL_LOCAL_DATE_TIME);
    private static final CruiseDateTime ANY_DEPARTURE_DATE_TIME = new CruiseDateTime(ANY_DEPARTURE_LOCAL_DATE_TIME);
    private static final Planet ANY_PLANET = new Planet(new PlanetName(ANY_PLANET_NAME), ANY_ARRIVAL_DATE_TIME, ANY_DEPARTURE_DATE_TIME);
    private PlanetService planetService;
    @Mock
    private CruiseRepository cruiseRepositoryMock;
    @Mock
    private LocalDateTimeParser localDateTimeParserMock;
    @Mock
    private PlanetAssembler planetAssemblerMock;
    @Mock
    private PlanetValidator planetValidatorMock;
    @Mock
    private Cruise cruiseMock;

    @BeforeEach
    void createPlanetService() {
        planetService = new PlanetService(cruiseRepositoryMock, localDateTimeParserMock, planetAssemblerMock,
                                          planetValidatorMock);
    }

    @Test
    void givenNonExistingCruise_whenAddPlanetToItinerary_thenThrowCruiseNotFoundException() {
        givenNonExistingCruise();

        Executable addPlanetToItinerary = () -> planetService.addPlanetToItinerary(ANY_CRUISE_ID_STR, ANY_PLANET_DTO);

        assertThrows(CruiseNotFoundException.class, addPlanetToItinerary);
    }

    @Test
    void givenExistingCruiseAndInvalidPlanet_whenAddPlanetToItinerary_thenThrowInvalidPlanetException() {
        givenExistingCruise();
        givenInvalidPlanet();

        Executable addPlanetToItinerary = () -> planetService.addPlanetToItinerary(ANY_CRUISE_ID_STR, ANY_PLANET_DTO);

        assertThrows(InvalidPlanetException.class, addPlanetToItinerary);
    }

    @Test
    void givenExistingCruiseAndValidPlanet_whenAddPlanetToItinerary_thenAddPlanetToItinerary() {
        givenExistingCruise();
        givenValidPlanet();

        planetService.addPlanetToItinerary(ANY_CRUISE_ID_STR, ANY_PLANET_DTO);

        verify(cruiseMock).addPlanetToItinerary(ANY_PLANET);
    }

    @Test
    void givenExistingCruiseAndValidPlanet_whenAddPlanetToItinerary_thenSaveCruise() {
        givenExistingCruise();
        givenValidPlanet();

        planetService.addPlanetToItinerary(ANY_CRUISE_ID_STR, ANY_PLANET_DTO);

        verify(cruiseRepositoryMock).save(cruiseMock);
    }

    private void givenNonExistingCruise() {
        when(cruiseRepositoryMock.findById(ANY_CRUISE_ID)).thenReturn(null);
    }

    private void givenExistingCruise() {
        when(cruiseRepositoryMock.findById(ANY_CRUISE_ID)).thenReturn(cruiseMock);
    }

    private void givenInvalidPlanet() {
        when(planetValidatorMock.isPlanetValid(ANY_PLANET_NAME)).thenReturn(false);
    }

    private void givenValidPlanet() {
        when(localDateTimeParserMock.parse(ANY_ARRIVAL_DATE_TIME_STR)).thenReturn(ANY_ARRIVAL_LOCAL_DATE_TIME);
        when(localDateTimeParserMock.parse(ANY_DEPARTURE_DATE_TIME_STR)).thenReturn(ANY_DEPARTURE_LOCAL_DATE_TIME);

        when(planetValidatorMock.isPlanetValid(ANY_PLANET_NAME)).thenReturn(true);
        when(planetAssemblerMock.toPlanet(ANY_PLANET_NAME, ANY_ARRIVAL_LOCAL_DATE_TIME, ANY_DEPARTURE_LOCAL_DATE_TIME)).thenReturn(ANY_PLANET);
    }
}