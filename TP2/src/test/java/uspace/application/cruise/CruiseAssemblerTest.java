package uspace.application.cruise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uspace.application.cruise.dtos.CruiseDto;
import uspace.application.cruise.planet.PlanetAssembler;
import uspace.application.cruise.planet.dtos.PlanetDto;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.crew.CrewMember;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.itinerary.planet.Planet;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CruiseAssemblerTest {

    private static final String CRUISE_ID_STRING = "JUPITER_MOONS_EXPLORATION_2085";
    private static final CruiseId CRUISE_ID = new CruiseId(CRUISE_ID_STRING);

    private static final LocalDateTime DEPARTURE_LOCAL_DATE_TIME = LocalDateTime.of(2085, 1, 25, 12, 0);
    private static final LocalDateTime END_LOCAL_DATE_TIME = LocalDateTime.of(2085, 2, 1, 12, 0);
    private static final CruiseDateTime DEPARTURE_DATE_TIME = new CruiseDateTime(DEPARTURE_LOCAL_DATE_TIME);
    private static final CruiseDateTime END_DATE_TIME = new CruiseDateTime(END_LOCAL_DATE_TIME);

    private static final String PLANET_ONE_NAME = "P1";
    private static final String PLANET_TWO_NAME = "P2";
    private static final String ARRIVAL_ONE = "A1";
    private static final String ARRIVAL_TWO = "A2";
    private static final String DEPARTURE_ONE = "D1";
    private static final String DEPARTURE_TWO = "D2";

    private static final String CREW_MEMBER_ONE_ID = "ABC123";
    private static final String CREW_MEMBER_TWO_ID = "ABD124";

    private static final int EXPECTED_PLANETS_COUNT = 2;
    private static final int EXPECTED_CREW_COUNT = 2;

    @Mock private PlanetAssembler planetAssembler;
    @Mock private Cruise cruise;
    @Mock private Planet planetOne;
    @Mock private Planet planetTwo;
    @Mock private CrewMember crewMemberOne;
    @Mock private CrewMember crewMemberTwo;

    private CruiseAssembler assembler;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        assembler = new CruiseAssembler(planetAssembler);

        when(cruise.getId()).thenReturn(CRUISE_ID);
        when(cruise.getDepartureDateTime()).thenReturn(DEPARTURE_DATE_TIME);
        when(cruise.getEndDateTime()).thenReturn(END_DATE_TIME);

        when(cruise.getPlanetsInItinerary()).thenReturn(List.of(planetOne, planetTwo));
        when(planetAssembler.toDto(planetOne))
                .thenReturn(new PlanetDto(PLANET_ONE_NAME, ARRIVAL_ONE, DEPARTURE_ONE, List.of()));
        when(planetAssembler.toDto(planetTwo))
                .thenReturn(new PlanetDto(PLANET_TWO_NAME, ARRIVAL_TWO, DEPARTURE_TWO, List.of()));

        when(crewMemberOne.getEmployeeId()).thenReturn(CREW_MEMBER_ONE_ID);
        when(crewMemberTwo.getEmployeeId()).thenReturn(CREW_MEMBER_TWO_ID);
        when(cruise.getCrewMembers()).thenReturn(List.of(crewMemberOne, crewMemberTwo));
    }

    @Test
    void whenToDto_thenPlanetsMappedCountIsTwo() {
        CruiseDto dto = assembler.toDto(cruise);

        assertEquals(EXPECTED_PLANETS_COUNT, dto.planets.size());
    }

    @Test
    void whenToDto_thenCrewMembersIdsMappedCountIsTwo() {
        CruiseDto dto = assembler.toDto(cruise);

        assertEquals(EXPECTED_CREW_COUNT, dto.crewMembersIds.size());
    }

    @Test
    void whenToDto_thenIdIsMappedFromCruiseIdToString() {
        CruiseDto dto = assembler.toDto(cruise);

        assertEquals(CRUISE_ID_STRING, dto.id);
    }
}
