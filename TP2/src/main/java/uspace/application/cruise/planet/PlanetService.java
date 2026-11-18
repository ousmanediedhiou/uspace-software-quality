package uspace.application.cruise.planet;

import jakarta.inject.Inject;
import uspace.application.cruise.planet.dtos.PlanetDto;
import uspace.application.utils.dateTimeParser.LocalDateTimeParser;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;
import uspace.domain.cruise.itinerary.planet.Planet;
import uspace.domain.cruise.itinerary.planet.PlanetValidator;
import uspace.domain.cruise.itinerary.planet.exceptions.InvalidPlanetException;

import java.time.LocalDateTime;

public class PlanetService {
    private final CruiseRepository cruiseRepository;
    private final LocalDateTimeParser localDateTimeParser;
    private final PlanetAssembler planetAssembler;
    private final PlanetValidator planetValidator;

    @Inject
    public PlanetService(CruiseRepository cruiseRepository, LocalDateTimeParser localDateTimeParser, PlanetAssembler planetAssembler,
                         PlanetValidator planetValidator) {
        this.cruiseRepository = cruiseRepository;
        this.localDateTimeParser = localDateTimeParser;
        this.planetAssembler = planetAssembler;
        this.planetValidator = planetValidator;
    }

    public void addPlanetToItinerary(String cruiseId, PlanetDto planetDto) {
        Cruise cruise = cruiseRepository.findById(new CruiseId(cruiseId));
        if (cruise == null) {
            throw new CruiseNotFoundException();
        }

        LocalDateTime arrivalDateTime = localDateTimeParser.parse(planetDto.arrivalDateTime);
        LocalDateTime departureDateTime = localDateTimeParser.parse(planetDto.departureDateTime);

        if (!planetValidator.isPlanetValid(planetDto.name)) {
            throw new InvalidPlanetException();
        }

        Planet planet = planetAssembler.toPlanet(planetDto.name, arrivalDateTime, departureDateTime);
        cruise.addPlanetToItinerary(planet);

        cruiseRepository.save(cruise);
    }
}
