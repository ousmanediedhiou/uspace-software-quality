package uspace.api.cruise.validators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.api.cruise.dtos.ZeroGravityExperienceBookingRequest;
import uspace.domain.exceptions.InvalidDateFormatException;
import uspace.domain.exceptions.MissingParameterException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ZeroGravityExperienceBookingRequestValidatorTest {

    @Test
    void givenMissingDate_whenValidate_thenThrowsMissingParameterException() {

        ZeroGravityExperienceBookingRequest dto = new ZeroGravityExperienceBookingRequest();
        dto.experienceBookingDateTime = null;

        assertThrows(MissingParameterException.class,
                () -> ZeroGravityExperienceBookingRequestValidator.validateAndParse(dto));
    }

    @Test
    void givenBadIsoFormat_whenValidate_thenThrowsInvalidDateFormatException() {

        ZeroGravityExperienceBookingRequest dto = new ZeroGravityExperienceBookingRequest();
        dto.experienceBookingDateTime = "08/04/2084 12:30";

        assertThrows(InvalidDateFormatException.class,
                () -> ZeroGravityExperienceBookingRequestValidator.validateAndParse(dto));
    }

    @Test
    void givenValidIsoFormat_whenValidate_thenReturnsParsedDate() {

        String iso = "2084-04-08T12:30";
        ZeroGravityExperienceBookingRequest dto = new ZeroGravityExperienceBookingRequest();
        dto.experienceBookingDateTime = iso;

        LocalDateTime result = ZeroGravityExperienceBookingRequestValidator.validateAndParse(dto);

        assertEquals(LocalDateTime.parse(iso), result);
    }
}
