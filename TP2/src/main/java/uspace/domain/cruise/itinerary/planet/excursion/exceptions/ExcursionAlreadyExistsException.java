package uspace.domain.cruise.itinerary.planet.excursion.exceptions;

public class ExcursionAlreadyExistsException extends RuntimeException {
    public ExcursionAlreadyExistsException() {
        super("An excursion with the same name already exists in this cruise.");
    }
}
