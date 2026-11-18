package uspace.api.cruise.booking.traveler.sport;

import uspace.domain.exceptions.MissingParameterException;

public class SportBookingRequestValidator {

    public void validate(SportBookingRequest request) {
        if (request == null || request.sport == null) {
            throw new MissingParameterException("sport");
        }
    }
}
