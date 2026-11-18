package uspace.application.cruise.planet.excursion;

import org.junit.jupiter.api.Test;
import uspace.application.cruise.planet.excursion.dtos.NewExcursionDto;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.itinerary.planet.excursion.Excursion;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExcursionAssemblerTest {

    private static final String ANY_NAME = "WalkToCanyon";
    private static final String ANY_DESC = "Explorez les crevasses glacées !";
    private static final LocalDateTime ANY_START = LocalDateTime.of(2085, 1, 28, 12, 0);
    private static final int ANY_DURATION = 2;
    private static final int ANY_CAPACITY = 10;

    private final ExcursionAssembler assembler = new ExcursionAssembler();

    private static NewExcursionDto newDto() {
        var dto = new NewExcursionDto();
        dto.name = ANY_NAME;
        dto.description = ANY_DESC;
        dto.startDateTime = "2085-01-28T12:00";
        dto.duration = ANY_DURATION;
        dto.capacity = ANY_CAPACITY;
        return dto;
    }

    @Test
    void whenToDomain_thenNameMapped() {
        var dto = newDto();

        Excursion domain = assembler.toDomain(dto, ANY_START);

        assertEquals(ANY_NAME, domain.getName());
    }

    @Test
    void whenToDomain_thenDescriptionMapped() {
        var dto = newDto();

        Excursion domain = assembler.toDomain(dto, ANY_START);

        assertEquals(ANY_DESC, domain.getDescription());
    }

    @Test
    void whenToDomain_thenStartDateTimeMapped() {
        var dto = newDto();

        Excursion domain = assembler.toDomain(dto, ANY_START);

        assertEquals(new CruiseDateTime(ANY_START), domain.getStartDateTime());
    }

    @Test
    void whenToDomain_thenDurationMapped() {
        var dto = newDto();

        Excursion domain = assembler.toDomain(dto, ANY_START);

        assertEquals(ANY_DURATION, domain.getDurationHours());
    }

    @Test
    void whenToDomain_thenCapacityMapped() {
        var dto = newDto();

        Excursion domain = assembler.toDomain(dto, ANY_START);

        assertEquals(ANY_CAPACITY, domain.getCapacity());
    }
}
