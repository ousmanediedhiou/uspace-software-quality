package uspace.domain.cruise.itinerary.planet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.itinerary.planet.excursion.Excursion;
import uspace.domain.cruise.itinerary.planet.excursion.exceptions.InvalidExcursionException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlanetExcursionRulesTest {

    private static final String PLANET_NAME_STR = "EUROPA";
    private static final PlanetName PLANET_NAME = new PlanetName(PLANET_NAME_STR);

    private static final LocalDateTime ARRIVAL_LDT   = LocalDateTime.of(2085, 1, 26, 9, 0);
    private static final LocalDateTime DEPARTURE_LDT = LocalDateTime.of(2085, 1, 30, 10, 0);
    private static final CruiseDateTime ARRIVAL   = new CruiseDateTime(ARRIVAL_LDT);
    private static final CruiseDateTime DEPARTURE = new CruiseDateTime(DEPARTURE_LDT);

    private static final String ANY_EXCURSION_NAME = "WalkToCanyon";
    private static final String ANY_EXCURSION_DESC = "Explore the ice cracks!";
    private static final int DURATION_HOURS_1 = 1;
    private static final int DURATION_HOURS_2 = 2;
    private static final int ANY_CAPACITY = 10;

    private static final String ERR_START_BEFORE_ARRIVAL   = "Excursion starts before planet arrival.";
    private static final String ERR_START_AFTER_DEPARTURE  = "Excursion starts after planet departure.";
    private static final String ERR_ENDS_AFTER_DEPARTURE   = "Excursion ends after planet departure.";

    private Planet planet;

    @BeforeEach
    void setup() {
        planet = new Planet(PLANET_NAME, ARRIVAL, DEPARTURE);
    }

    @Test
    void addExcursion_whenStartInsideWindowAndEndBeforeDeparture_thenAdded() {
        LocalDateTime startInsideWindow = ARRIVAL_LDT.plusDays(2).withHour(12).withMinute(0);
        Excursion excursion = new Excursion(
                ANY_EXCURSION_NAME,
                ANY_EXCURSION_DESC,
                new CruiseDateTime(startInsideWindow),
                DURATION_HOURS_2,
                ANY_CAPACITY
        );

        planet.addExcursion(excursion);

        assertEquals(1, planet.getExcursions().size());
    }

    @Test
    void addExcursion_whenStartBeforeArrival_thenThrowsInvalidExcursion() {
        LocalDateTime startBeforeArrival = ARRIVAL_LDT.minusHours(1);
        Excursion excursion = new Excursion(
                "TooEarly",
                "Before arrival",
                new CruiseDateTime(startBeforeArrival),
                DURATION_HOURS_2,
                ANY_CAPACITY
        );

        InvalidExcursionException ex =
                assertThrows(InvalidExcursionException.class, () -> planet.addExcursion(excursion));

        assertEquals(ERR_START_BEFORE_ARRIVAL, ex.getMessage());
    }

    @Test
    void addExcursion_whenStartAfterDeparture_thenThrowsInvalidExcursion() {
        LocalDateTime startAfterDeparture = DEPARTURE_LDT.plusMinutes(1);
        Excursion excursion = new Excursion(
                "TooLate",
                "After departure",
                new CruiseDateTime(startAfterDeparture),
                DURATION_HOURS_2,
                ANY_CAPACITY
        );

        InvalidExcursionException ex =
                assertThrows(InvalidExcursionException.class, () -> planet.addExcursion(excursion));

        assertEquals(ERR_START_AFTER_DEPARTURE, ex.getMessage());
    }

    @Test
    void addExcursion_whenEndAfterDeparture_thenThrowsInvalidExcursion() {
        LocalDateTime startEndsAfterDeparture = DEPARTURE_LDT.minusMinutes(30);
        Excursion excursion = new Excursion(
                "TooLong",
                "Ends after departure",
                new CruiseDateTime(startEndsAfterDeparture),
                DURATION_HOURS_1,
                ANY_CAPACITY
        );

        InvalidExcursionException ex =
                assertThrows(InvalidExcursionException.class, () -> planet.addExcursion(excursion));

        assertEquals(ERR_ENDS_AFTER_DEPARTURE, ex.getMessage());
    }
}
