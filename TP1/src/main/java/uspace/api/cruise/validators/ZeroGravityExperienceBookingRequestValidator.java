package uspace.api.cruise.validators;

import uspace.api.cruise.dtos.ZeroGravityExperienceBookingRequest;
import uspace.domain.exceptions.InvalidDateFormatException;
import uspace.domain.exceptions.MissingParameterException;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class ZeroGravityExperienceBookingRequestValidator {

    public static LocalDateTime validateAndParse(ZeroGravityExperienceBookingRequest dto) {
        if (dto == null || dto.experienceBookingDateTime == null || dto.experienceBookingDateTime.isBlank()) {
            throw new MissingParameterException("experienceBookingDateTime");
        }

        try {
            return LocalDateTime.parse(dto.experienceBookingDateTime);
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException();
        }
    }
}
