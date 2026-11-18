package uspace.application.cruise.planet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uspace.application.cruise.planet.dtos.PlanetDto;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.itinerary.planet.Planet;
import uspace.domain.cruise.itinerary.planet.PlanetName;
import uspace.domain.cruise.itinerary.planet.excursion.Excursion;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlanetAssemblerTest {

    private static final String ANY_PLANET_NAME_STR = "ANY_PLANET_NAME";
    private static final PlanetName ANY_PLANET_NAME = new PlanetName(ANY_PLANET_NAME_STR);
    private static final LocalDateTime ANY_ARRIVAL_LOCAL_DATE_TIME = LocalDateTime.of(2084, 4, 8, 12, 30);
    private static final LocalDateTime ANY_DEPARTURE_LOCAL_DATE_TIME = LocalDateTime.of(2084, 6, 8, 13, 30);
    private static final CruiseDateTime ANY_ARRIVAL_DATE_TIME = new CruiseDateTime(ANY_ARRIVAL_LOCAL_DATE_TIME);
    private static final CruiseDateTime ANY_DEPARTURE_DATE_TIME = new CruiseDateTime(ANY_DEPARTURE_LOCAL_DATE_TIME);

    private PlanetAssembler planetAssembler;

    @BeforeEach
    void createPlanetAssembler() {
        planetAssembler = new PlanetAssembler();
    }

    @Test
    void whenToPlanet_thenConvertPlanetDtoToPlanet() {
        Planet planet = planetAssembler.toPlanet(ANY_PLANET_NAME_STR, ANY_ARRIVAL_LOCAL_DATE_TIME, ANY_DEPARTURE_LOCAL_DATE_TIME);

        assertEquals(ANY_PLANET_NAME, planet.getName());
        assertEquals(ANY_ARRIVAL_DATE_TIME, planet.getArrivalDate());
        assertEquals(ANY_DEPARTURE_DATE_TIME, planet.getDepartureDate());
    }

    @Test
    void whenNoExcursions_thenDtoExcursionsIsEmpty() {
        Planet planet = new Planet(ANY_PLANET_NAME, ANY_ARRIVAL_DATE_TIME, ANY_DEPARTURE_DATE_TIME);

        PlanetDto dto = planetAssembler.toDto(planet);

        assertEquals(0, dto.excursions.size());
    }

    @Test
    void whenTwoExcursions_thenDtoExcursionsSizeIsTwo() {
        Planet planet = new Planet(ANY_PLANET_NAME, ANY_ARRIVAL_DATE_TIME, ANY_DEPARTURE_DATE_TIME);
        planet.addExcursion(new Excursion("Walk", "Desc", new CruiseDateTime(LocalDateTime.of(2084, 4, 9, 10, 0)), 2, 5));
        planet.addExcursion(new Excursion("Run", "Desc2", new CruiseDateTime(LocalDateTime.of(2084, 4, 10, 11, 0)), 3, 8));

        PlanetDto dto = planetAssembler.toDto(planet);

        assertEquals(2, dto.excursions.size());
    }

    @Test
    void whenExcursionMapped_thenNameIsExact() {
        Planet planet = new Planet(ANY_PLANET_NAME, ANY_ARRIVAL_DATE_TIME, ANY_DEPARTURE_DATE_TIME);
        planet.addExcursion(new Excursion("Mars Walk", "Desc", new CruiseDateTime(LocalDateTime.of(2084, 4, 9, 10, 0)), 2, 5));

        PlanetDto dto = planetAssembler.toDto(planet);

        assertEquals("Mars Walk", dto.excursions.get(0).name);
    }
}
