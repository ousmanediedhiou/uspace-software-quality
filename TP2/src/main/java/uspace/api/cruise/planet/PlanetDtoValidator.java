package uspace.api.cruise.planet;

import uspace.application.cruise.planet.dtos.PlanetDto;
import uspace.domain.exceptions.MissingParameterException;

public class PlanetDtoValidator {
    public void validate(PlanetDto planetDto) {
        if (planetDto.name == null) {
            throw new MissingParameterException("name");
        }
        if (planetDto.arrivalDateTime == null) {
            throw new MissingParameterException("arrivalDateTime");
        }
        if (planetDto.departureDateTime == null) {
            throw new MissingParameterException("departureDateTime");
        }
    }
}
