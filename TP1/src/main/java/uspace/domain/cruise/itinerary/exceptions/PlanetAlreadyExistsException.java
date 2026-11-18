package uspace.domain.cruise.itinerary.exceptions;

import uspace.domain.exceptions.InvalidParameterException;

public class PlanetAlreadyExistsException extends InvalidParameterException {
    public PlanetAlreadyExistsException() {
        super("Planet already exists in the itinerary");
    }
}
