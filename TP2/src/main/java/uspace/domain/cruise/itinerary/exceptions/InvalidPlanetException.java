package uspace.domain.cruise.itinerary.exceptions;

public class InvalidPlanetException extends RuntimeException {
    public InvalidPlanetException() {
        super("Planet not found in itinerary.");
    }
}
