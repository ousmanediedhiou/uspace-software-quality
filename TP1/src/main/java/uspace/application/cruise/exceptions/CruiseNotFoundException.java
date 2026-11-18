package uspace.application.cruise.exceptions;


public class CruiseNotFoundException extends RuntimeException {
    public CruiseNotFoundException(String cruiseId) {
        super("Cruise not found: " + cruiseId);
    }
}

