package uspace.api.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import uspace.application.account.dtos.AccountDto;
import uspace.domain.exceptions.MissingParameterException;

import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountDtoValidatorTest {

    private static final String ANY_USERNAME = "ANY_USERNAME";
    private static final String ANY_PLANET = "ANY_PLANET";

    private AccountDtoValidator validator;

    @BeforeEach
    void setUp() {
        validator = new AccountDtoValidator();
    }

    @Test
    void givenMissingUsername_whenValidate_thenThrowMissingParameterException() {
        AccountDto dto = new AccountDto();
        dto.username = null;
        dto.homePlanetName = ANY_PLANET;

        Executable validate = () -> validator.validate(dto);

        assertThrows(MissingParameterException.class, validate);
    }

    @Test
    void givenEmptyUsername_whenValidate_thenThrowMissingParameterException() {
        AccountDto dto = new AccountDto();
        dto.username = "";
        dto.homePlanetName = ANY_PLANET;

        Executable validate = () -> validator.validate(dto);

        assertThrows(MissingParameterException.class, validate);
    }

    @Test
    void givenMissingHomePlanetName_whenValidate_thenThrowMissingParameterException() {
        AccountDto dto = new AccountDto();
        dto.username = ANY_USERNAME;
        dto.homePlanetName = null;

        Executable validate = () -> validator.validate(dto);

        assertThrows(MissingParameterException.class, validate);
    }

    @Test
    void givenEmptyHomePlanetName_whenValidate_thenThrowMissingParameterException() {
        AccountDto dto = new AccountDto();
        dto.username = ANY_USERNAME;
        dto.homePlanetName = "";

        Executable validate = () -> validator.validate(dto);

        assertThrows(MissingParameterException.class, validate);
    }
}
