package uspace.application.cruise.planet.excursion;

import uspace.application.cruise.planet.excursion.dtos.NewExcursionDto;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.itinerary.planet.excursion.Excursion;

import java.time.LocalDateTime;

public class ExcursionAssembler {
    public Excursion toDomain(NewExcursionDto dto, LocalDateTime start) {
        return new Excursion(
                dto.name,
                dto.description,
                new CruiseDateTime(start),
                dto.duration,
                dto.capacity
        );
    }
}
