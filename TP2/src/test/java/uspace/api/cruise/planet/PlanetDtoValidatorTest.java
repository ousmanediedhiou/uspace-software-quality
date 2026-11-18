package uspace.api.cruise.planet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import uspace.application.cruise.planet.dtos.PlanetDto;
import uspace.domain.exceptions.MissingParameterException;

import static org.junit.jupiter.api.Assertions.*;

class PlanetDtoValidatorTest {

    private static final String ANY_PLANET_NAME = "ANY_PLANET_NAME";
    private static final String ANY_LOCAL_DATE_TIME = "2084-04-08T12:30";
    private PlanetDtoValidator planetDtoValidator;

    @BeforeEach
    void createPlanetDtoValidator() {
        planetDtoValidator = new PlanetDtoValidator();
    }

    @Test
    void givenPlanetDtoWithoutName_whenValidate_thenThrowMissingParameterException() {
        PlanetDto planetDto = new PlanetDto(null, ANY_LOCAL_DATE_TIME, ANY_LOCAL_DATE_TIME);

        Executable validate = () -> planetDtoValidator.validate(planetDto);

        assertThrows(MissingParameterException.class, validate);
    }

    @Test
    void givenPlanetDtoWithoutArrivalDateTime_whenValidate_thenThrowMissingParameterException() {
        PlanetDto planetDto = new PlanetDto(ANY_PLANET_NAME, null, ANY_LOCAL_DATE_TIME);

        Executable validate = () -> planetDtoValidator.validate(planetDto);

        assertThrows(MissingParameterException.class, validate);
    }

    @Test
    void givenPlanetDtoWithoutDepartureDateTime_whenValidate_thenThrowMissingParameterException() {
        PlanetDto planetDto = new PlanetDto(ANY_PLANET_NAME, ANY_LOCAL_DATE_TIME, null);

        Executable validate = () -> planetDtoValidator.validate(planetDto);

        assertThrows(MissingParameterException.class, validate);
    }

    @Test
    void givenPlanetDtoWithAllParameters_whenValidate_thenDoNothing() {
        PlanetDto planetDto = new PlanetDto(ANY_PLANET_NAME, ANY_LOCAL_DATE_TIME, ANY_LOCAL_DATE_TIME);

        Executable validate = () -> planetDtoValidator.validate(planetDto);

        assertDoesNotThrow(validate);
    }
}