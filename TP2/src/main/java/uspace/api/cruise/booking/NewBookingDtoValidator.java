package uspace.api.cruise.booking;

import uspace.application.cruise.booking.dtos.newBooking.NewBookingDto;
import uspace.domain.exceptions.MissingParameterException;

public class NewBookingDtoValidator {

    public void validate(NewBookingDto dto) {
        if (dto == null) {
            throw new MissingParameterException("body");
        }
        if (dto.accountUsername == null || dto.accountUsername.isBlank()) {
            throw new MissingParameterException("accountUsername");
        }
        if (dto.bookingDateTime == null || dto.bookingDateTime.isBlank()) {
            throw new MissingParameterException("bookingDateTime");
        }
        if (dto.cabinType == null || dto.cabinType.isBlank()) {
            throw new MissingParameterException("cabinType");
        }
        if (dto.travelers == null || dto.travelers.isEmpty()) {
            throw new MissingParameterException("travelers");
        }
        dto.travelers.forEach(traveler -> {
            if (traveler.name == null || traveler.name.isBlank()) {
                throw new MissingParameterException("name");
            }
            if (traveler.category == null || traveler.category.isBlank()) {
                throw new MissingParameterException("category");
            }
        });
    }
}
