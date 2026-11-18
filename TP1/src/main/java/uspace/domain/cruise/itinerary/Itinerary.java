package uspace.domain.cruise.itinerary;

import uspace.domain.cruise.itinerary.planet.Planet;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.itinerary.exceptions.InvalidPlanetDateException;
import uspace.domain.cruise.itinerary.exceptions.PlanetAlreadyExistsException;

import java.util.List;

public class Itinerary {
    private final List<Planet> planets;

    public Itinerary(List<Planet> planets) {
        this.planets = planets;
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    public void addPlanet(Planet newPlanet,
                          CruiseDateTime cruiseDepartureDateTime,
                          CruiseDateTime cruiseEndDateTime) {

        boolean alreadyExists = planets.stream()
                .anyMatch(p -> p.getName().equals(newPlanet.getName()));
        if (alreadyExists) {
            throw new PlanetAlreadyExistsException();
        }

        var arrival   = newPlanet.getArrivalDate();
        var departure = newPlanet.getDepartureDate();

        if (!arrival.isAfter(cruiseDepartureDateTime)) {
            throw new InvalidPlanetDateException();
        }
        if (!departure.isBefore(cruiseEndDateTime)) {
            throw new InvalidPlanetDateException();
        }
        if (!departure.isAfter(arrival)) {
            throw new InvalidPlanetDateException();
        }

        for (Planet existing : planets) {
            var a1 = arrival;
            var b1 = departure;
            var a2 = existing.getArrivalDate();
            var b2 = existing.getDepartureDate();

            boolean overlaps = !(b1.isBefore(a2) || a1.isAfter(b2));
            if (overlaps) {
                throw new InvalidPlanetDateException();
            }
        }

        planets.add(newPlanet);
    }
}
