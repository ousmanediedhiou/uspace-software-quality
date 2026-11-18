package uspace.domain.cruise.itinerary.exceptions;

import uspace.domain.exceptions.InvalidParameterException;

public class InvalidPlanetDateException extends InvalidParameterException {
    public InvalidPlanetDateException() {
        super("Arrival date or departure date is invalid");
    }
}
