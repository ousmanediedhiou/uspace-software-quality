package uspace.application.cruise.planet;

import uspace.application.cruise.planet.dtos.ExcursionDto;
import uspace.application.cruise.planet.dtos.PlanetDto;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.itinerary.planet.Planet;
import uspace.domain.cruise.itinerary.planet.PlanetName;
import uspace.domain.cruise.itinerary.planet.excursion.Excursion;

import java.time.LocalDateTime;
import java.util.List;

public class PlanetAssembler {

    public PlanetDto toDto(Planet planet) {
        List<ExcursionDto> excursionDtos = planet.getExcursions()
                .stream()
                .map(this::toExcursionDto)
                .toList();

        return new PlanetDto(
                planet.getName().toString(),
                planet.getArrivalDate().toString(),
                planet.getDepartureDate().toString(),
                excursionDtos
        );
    }

    private ExcursionDto toExcursionDto(Excursion excursion) {
        ExcursionDto dto = new ExcursionDto();
        dto.name = excursion.getName();
        dto.description = excursion.getDescription();
        dto.startDateTime = excursion.getStartDateTime().toString();
        dto.duration = excursion.getDurationHours();
        dto.capacity = excursion.getCapacity();
        return dto;
    }

    public Planet toPlanet(String name, LocalDateTime arrivalLocalDateTime, LocalDateTime departureLocalDateTime) {
        PlanetName planetName = new PlanetName(name);
        CruiseDateTime arrivalDateTime = new CruiseDateTime(arrivalLocalDateTime);
        CruiseDateTime departureDateTime = new CruiseDateTime(departureLocalDateTime);
        return new Planet(planetName, arrivalDateTime, departureDateTime);
    }
}
