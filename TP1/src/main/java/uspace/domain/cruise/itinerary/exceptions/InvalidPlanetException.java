package uspace.domain.cruise.itinerary.exceptions;

import uspace.domain.exceptions.InvalidParameterException;

public class InvalidPlanetException extends InvalidParameterException {
    public InvalidPlanetException() {
        super("Invalid planet");
    }
}

