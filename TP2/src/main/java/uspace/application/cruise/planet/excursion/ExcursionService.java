package uspace.application.cruise.planet.excursion;

import jakarta.inject.Inject;
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

public class ExcursionService {

    private final CruiseRepository cruiseRepository;
    private final ExcursionAssembler assembler;
    private final LocalDateTimeParser dateTimeParser;

    @Inject
    public ExcursionService(CruiseRepository cruiseRepository,
                            ExcursionAssembler assembler,
                            LocalDateTimeParser dateTimeParser) {
        this.cruiseRepository = cruiseRepository;
        this.assembler = assembler;
        this.dateTimeParser = dateTimeParser;
    }

    public void addExcursionToPlanet(String cruiseId, String planetName, NewExcursionDto dto) {
        validateDto(dto);

        Cruise cruise = cruiseRepository.findById(new CruiseId(cruiseId));
        if (cruise == null) {
            throw new CruiseNotFoundException();
        }

        LocalDateTime startDateTime;
        try {
            startDateTime = dateTimeParser.parse(dto.startDateTime);
        } catch (Exception e) {
            throw new InvalidDateFormatException();
        }

        Excursion excursion = assembler.toDomain(dto, startDateTime);
        cruise.addExcursionToPlanet(planetName, excursion);
        cruiseRepository.save(cruise);
    }

    private void validateDto(NewExcursionDto dto) {
        if (dto.name == null || dto.name.trim().isEmpty())
            throw new MissingParameterException("name");
        if (dto.description == null || dto.description.trim().isEmpty())
            throw new MissingParameterException("description");
        if (dto.startDateTime == null || dto.startDateTime.trim().isEmpty())
            throw new MissingParameterException("startDateTime");
        if (dto.duration == null)
            throw new MissingParameterException("duration");
        if (dto.capacity == null)
            throw new MissingParameterException("capacity");
        if (dto.duration <= 0)
            throw new InvalidParameterException("Excursion duration must be greater than 0.");
        if (dto.capacity <= 0)
            throw new InvalidParameterException("Excursion capacity must be greater than 0.");
    }
}
