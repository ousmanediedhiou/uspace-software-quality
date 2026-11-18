package uspace.api.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import uspace.application.account.dtos.NewAccountDto;
import uspace.domain.exceptions.MissingParameterException;

import static org.junit.jupiter.api.Assertions.*;

class NewAccountDtoValidatorTest {

    private static final String VALID_USERNAME = "john_doe";
    private static final String VALID_HOME_PLANET = "Earth";

    private NewAccountDtoValidator validator;

    @BeforeEach
    void setup() {
        validator = new NewAccountDtoValidator();
    }

    @Test
    void givenMissingUsername_whenValidate_thenThrowMissingParameterException() {
        NewAccountDto dto = new NewAccountDto();
        dto.username = null;
        dto.homePlanetName = VALID_HOME_PLANET;

        Executable call = () -> validator.validate(dto);

        assertThrows(MissingParameterException.class, call);
    }

    @Test
    void givenMissingHomePlanet_whenValidate_thenThrowMissingParameterException() {
        NewAccountDto dto = new NewAccountDto();
        dto.username = VALID_USERNAME;
        dto.homePlanetName = null;

        Executable call = () -> validator.validate(dto);

        assertThrows(MissingParameterException.class, call);
    }

    @Test
    void givenAllValidFields_whenValidate_thenNoExceptionThrown() {
        NewAccountDto dto = new NewAccountDto();
        dto.username = VALID_USERNAME;
        dto.homePlanetName = VALID_HOME_PLANET;

        assertDoesNotThrow(() -> validator.validate(dto));
    }
}
