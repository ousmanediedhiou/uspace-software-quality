package uspace.domain.cruise.emergencyShuttles.exceptions;

import uspace.domain.exceptions.InvalidParameterException;

public class InvalidShuttleTypeException extends InvalidParameterException {

    public InvalidShuttleTypeException() {
        super("Invalid shuttle type.");
    }
}
