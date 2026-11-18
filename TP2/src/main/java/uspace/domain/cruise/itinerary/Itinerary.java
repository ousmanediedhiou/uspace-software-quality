package uspace.domain.cruise.itinerary;

import jakarta.persistence.*;
import uspace.domain.cruise.itinerary.exceptions.InvalidPlanetDateException;
import uspace.domain.cruise.itinerary.exceptions.PlanetAlreadyExistsException;
import uspace.domain.cruise.itinerary.planet.Planet;

import java.util.List;

@Entity
public class Itinerary {
    @Id
    private ItineraryId id;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Planet> planets;
    private int minimalDaysForPlanetExcursion;

    public Itinerary() {
    }

    public Itinerary(ItineraryId id, List<Planet> planets, int minimalDaysForPlanetExcursion) {
        this.id = id;
        this.planets = planets;
        this.minimalDaysForPlanetExcursion = minimalDaysForPlanetExcursion;
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    public void addPlanet(Planet newPlanet) {
        if (containsPlanetName(newPlanet)) {
            throw new PlanetAlreadyExistsException();
        }

        if (isPlanetExcursionLessThanMinimalDays(newPlanet) || containsPlanetWithSameTimeSlot(newPlanet)) {
            throw new InvalidPlanetDateException();
        }

        planets.add(newPlanet);
    }

    private boolean containsPlanetName(Planet newPlanet) {
        return planets.stream().anyMatch(newPlanet::hasSameNameThan);
    }

    private boolean isPlanetExcursionLessThanMinimalDays(Planet newPlanet) {
        return newPlanet.computeExcursionDurationInDays() < minimalDaysForPlanetExcursion;
    }

    private boolean containsPlanetWithSameTimeSlot(Planet newPlanet) {
        return planets.stream().anyMatch(newPlanet::hasSameTimeSlotThan);
    }
}
