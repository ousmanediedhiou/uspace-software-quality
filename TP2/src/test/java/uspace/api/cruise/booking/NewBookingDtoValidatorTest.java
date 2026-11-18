package uspace.api.cruise.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import uspace.application.cruise.booking.dtos.newBooking.NewBookingDto;
import uspace.application.cruise.booking.dtos.newBooking.NewBookingTravelerDto;
import uspace.domain.exceptions.MissingParameterException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NewBookingDtoValidatorTest {

    private static final String VALID_CABIN_TYPE = "STANDARD";
    private static final String VALID_TRAVELER_NAME = "A1";
    private static final String VALID_TRAVELER_CATEGORY = "ADULT";
    private static final String VALID_ACCOUNT_USERNAME = "john_doe";
    private static final String VALID_BOOKING_DATE_TIME = "2085-01-15T12:00";

    private NewBookingDtoValidator validator;

    @BeforeEach
    void setup() {
        validator = new NewBookingDtoValidator();
    }


    @Test
    void givenMissingAccountUsername_whenValidate_thenThrowMissingParameterException() {
        NewBookingTravelerDto traveler =
                new NewBookingTravelerDto(VALID_TRAVELER_NAME, VALID_TRAVELER_CATEGORY);
        NewBookingDto dto =
                new NewBookingDto(VALID_CABIN_TYPE, List.of(traveler), VALID_BOOKING_DATE_TIME);
        dto.accountUsername = null; // cas de test

         Executable call = () -> validator.validate(dto);

        assertThrows(MissingParameterException.class, call);
    }

    @Test
    void givenAllValidFields_whenValidate_thenNoExceptionThrown() {
       NewBookingTravelerDto traveler =
                new NewBookingTravelerDto(VALID_TRAVELER_NAME, VALID_TRAVELER_CATEGORY);
        NewBookingDto dto =
                new NewBookingDto(VALID_CABIN_TYPE, List.of(traveler), VALID_BOOKING_DATE_TIME);
        dto.accountUsername = VALID_ACCOUNT_USERNAME;

        assertDoesNotThrow(() -> validator.validate(dto));
    }
}
