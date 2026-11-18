package uspace.application.planet;

import jakarta.inject.Inject;
import uspace.application.cruise.exceptions.CruiseNotFoundException;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.itinerary.Itinerary;
import uspace.domain.cruise.itinerary.exceptions.InvalidPlanetDateException;
import uspace.domain.cruise.itinerary.exceptions.InvalidPlanetException;
import uspace.domain.cruise.itinerary.planet.Planet;
import uspace.domain.cruise.itinerary.planet.PlanetName;

import java.time.Duration;

public class PlanetService {

    private final CruiseRepository cruiseRepository;
    private final PlanetValidator planetValidator;

    @Inject
    public PlanetService(CruiseRepository cruiseRepository, PlanetValidator planetValidator) {
        this.cruiseRepository = cruiseRepository;
        this.planetValidator = planetValidator;
    }


    public void addPlanet(String cruiseId, NewPlanetDto dto) {
        if (isBlank(cruiseId) || dto == null || isBlank(dto.name()) || dto.arrival() == null || dto.departure() == null) {
            throw new IllegalArgumentException("Missing parameter(s)");
        }

        if (Duration.between(dto.arrival(), dto.departure()).toHours() < 72) {
            throw new InvalidPlanetDateException();
        }

        if (!planetValidator.isValid(dto.name())) {
            throw new InvalidPlanetException();
        }

        Cruise cruise = cruiseRepository.findById(new CruiseId(cruiseId));
        if (cruise == null) {
            throw new CruiseNotFoundException(cruiseId);
        }

        CruiseDateTime cruiseStart = cruise.getDepartureDateTime();
        CruiseDateTime cruiseEnd   = cruise.getEndDateTime();

        Itinerary itinerary = new Itinerary(cruise.getPlanetsInItinerary());

        Planet planet = new Planet(
                new PlanetName(dto.name()),
                new CruiseDateTime(dto.arrival()),
                new CruiseDateTime(dto.departure())
        );

        itinerary.addPlanet(planet, cruiseStart, cruiseEnd);
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
