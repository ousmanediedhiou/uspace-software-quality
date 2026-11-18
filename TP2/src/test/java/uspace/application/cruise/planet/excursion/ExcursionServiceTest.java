package uspace.application.cruise.planet.excursion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.application.cruise.planet.excursion.dtos.NewExcursionDto;
import uspace.application.utils.dateTimeParser.LocalDateTimeParser;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;
import uspace.domain.cruise.itinerary.planet.excursion.Excursion;
import uspace.domain.exceptions.InvalidDateFormatException;
import uspace.domain.exceptions.InvalidParameterException;
import uspace.domain.exceptions.MissingParameterException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExcursionServiceTest {

    private static final String ANY_CRUISE_ID = "JUPITER_MOONS_EXPLORATION_2085";
    private static final CruiseId ANY_CRUISE_ID_VO = new CruiseId(ANY_CRUISE_ID);
    private static final String ANY_PLANET_NAME = "EUROPA";
    private static final String ANY_EXCURSION_NAME = "WalkToCanyon";
    private static final String ANY_EXCURSION_DESC = "Explorez les crevasses glacées !";
    private static final String ANY_START_DATETIME_TEXT = "2085-01-28T12:00";
    private static final LocalDateTime ANY_START_DATETIME = LocalDateTime.of(2085, 1, 28, 12, 0);
    private static final int ANY_DURATION_HOURS = 2;
    private static final int ANY_CAPACITY = 10;

    @Mock private CruiseRepository cruiseRepository;
    @Mock private ExcursionAssembler assembler;
    @Mock private LocalDateTimeParser dateTimeParser;
    @Mock private Cruise cruise;
    @Mock private Excursion built;

    @InjectMocks
    private ExcursionService service;

    @BeforeEach
    void init() {
    }

    private static NewExcursionDto validDto() {
        var dto = new NewExcursionDto();
        dto.name = ANY_EXCURSION_NAME;
        dto.description = ANY_EXCURSION_DESC;
        dto.startDateTime = ANY_START_DATETIME_TEXT;
        dto.duration = ANY_DURATION_HOURS;
        dto.capacity = ANY_CAPACITY;
        return dto;
    }

    @Test
    void givenValidRequest_whenAddExcursion_thenCallsDomainAndSaves() {
        var dto = validDto();

        when(cruiseRepository.findById(ANY_CRUISE_ID_VO)).thenReturn(cruise);
        when(dateTimeParser.parse(ANY_START_DATETIME_TEXT)).thenReturn(ANY_START_DATETIME);
        when(assembler.toDomain(dto, ANY_START_DATETIME)).thenReturn(built);

        service.addExcursionToPlanet(ANY_CRUISE_ID, ANY_PLANET_NAME, dto);

        verify(cruise).addExcursionToPlanet(ANY_PLANET_NAME, built);
        verify(cruiseRepository).save(cruise);
    }

    @Test
    void whenNameMissing_thenMissingParameter_name() {
        var dto = validDto();
        dto.name = null;

        var ex = assertThrows(MissingParameterException.class,
                () -> service.addExcursionToPlanet(ANY_CRUISE_ID, ANY_PLANET_NAME, dto));

        assertEquals("Missing parameter: name", ex.getMessage());
    }

    @Test
    void whenDescriptionBlank_thenMissingParameter_description() {
        var dto = validDto();
        dto.description = "  ";

        var ex = assertThrows(MissingParameterException.class,
                () -> service.addExcursionToPlanet(ANY_CRUISE_ID, ANY_PLANET_NAME, dto));

        assertEquals("Missing parameter: description", ex.getMessage());
    }

    @Test
    void whenStartDateTimeMissing_thenMissingParameter_startDateTime() {
        var dto = validDto();
        dto.startDateTime = null;

        var ex = assertThrows(MissingParameterException.class,
                () -> service.addExcursionToPlanet(ANY_CRUISE_ID, ANY_PLANET_NAME, dto));

        assertEquals("Missing parameter: startDateTime", ex.getMessage());
    }

    @Test
    void whenDurationMissing_thenMissingParameter_duration() {
        var dto = validDto();
        dto.duration = null;

        var ex = assertThrows(MissingParameterException.class,
                () -> service.addExcursionToPlanet(ANY_CRUISE_ID, ANY_PLANET_NAME, dto));

        assertEquals("Missing parameter: duration", ex.getMessage());
    }

    @Test
    void whenCapacityMissing_thenMissingParameter_capacity() {
        var dto = validDto();
        dto.capacity = null;

        var ex = assertThrows(MissingParameterException.class,
                () -> service.addExcursionToPlanet(ANY_CRUISE_ID, ANY_PLANET_NAME, dto));

        assertEquals("Missing parameter: capacity", ex.getMessage());
    }

    @Test
    void whenDurationNotPositive_thenInvalidParameter_messageExact() {
        var dto = validDto();
        dto.duration = 0;

        var ex = assertThrows(InvalidParameterException.class,
                () -> service.addExcursionToPlanet(ANY_CRUISE_ID, ANY_PLANET_NAME, dto));

        assertEquals("Excursion duration must be greater than 0.", ex.getMessage());
    }

    @Test
    void whenCapacityNotPositive_thenInvalidParameter_messageExact() {
        var dto = validDto();
        dto.capacity = -1;

        var ex = assertThrows(InvalidParameterException.class,
                () -> service.addExcursionToPlanet(ANY_CRUISE_ID, ANY_PLANET_NAME, dto));

        assertEquals("Excursion capacity must be greater than 0.", ex.getMessage());
    }

    @Test
    void whenCruiseNotFound_thenThrowsCruiseNotFound() {
        var dto = validDto();

        when(cruiseRepository.findById(ANY_CRUISE_ID_VO)).thenReturn(null);

        assertThrows(CruiseNotFoundException.class,
                () -> service.addExcursionToPlanet(ANY_CRUISE_ID, ANY_PLANET_NAME, dto));
    }

    @Test
    void whenDateHasWrongFormat_thenInvalidDateFormat() {
        var dto = validDto();
        dto.startDateTime = "28-01-2085 12:00";

        when(cruiseRepository.findById(ANY_CRUISE_ID_VO)).thenReturn(cruise);
        when(dateTimeParser.parse(dto.startDateTime)).thenThrow(new RuntimeException("parse error"));

        assertThrows(InvalidDateFormatException.class,
                () -> service.addExcursionToPlanet(ANY_CRUISE_ID, ANY_PLANET_NAME, dto));
    }
}
