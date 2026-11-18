package uspace.domain.cruise.itinerary.planet;

import jakarta.persistence.*;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import java.util.ArrayList;
import java.util.List;
import uspace.domain.cruise.itinerary.planet.excursion.Excursion;
import uspace.domain.cruise.itinerary.planet.excursion.exceptions.InvalidExcursionException;

@Entity
public class Planet {

    @EmbeddedId
    private PlanetName name;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "localDateTime", column = @Column(name = "arrivalDateTime"))
    })
    private CruiseDateTime arrivalDateTime;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "localDateTime", column = @Column(name = "departureDateTime"))
    })
    private CruiseDateTime departureDateTime;

    @OneToMany(mappedBy = "planet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Excursion> excursions = new ArrayList<>();

    protected Planet() { }

    public Planet(PlanetName name, CruiseDateTime arrivalDateTime, CruiseDateTime departureDateTime) {
        this.name = name;
        this.arrivalDateTime = arrivalDateTime;
        this.departureDateTime = departureDateTime;
    }

    public PlanetName getName() { return name; }
    public CruiseDateTime getArrivalDate() { return arrivalDateTime; }
    public CruiseDateTime getDepartureDate() { return departureDateTime; }
    public List<Excursion> getExcursions() { return excursions; }

    public boolean hasSameNameThan(Planet otherPlanet) { return name.equals(otherPlanet.name); }
    public boolean arriveBefore(CruiseDateTime dateTime) { return arrivalDateTime.isBefore(dateTime); }
    public boolean departAfter(CruiseDateTime dateTime) { return departureDateTime.isAfter(dateTime); }
    public boolean hasSameTimeSlotThan(Planet otherPlanet) { return arriveInTimeSlot(otherPlanet) || departInTimeSlot(otherPlanet); }
    public int computeExcursionDurationInDays() { return arrivalDateTime.calculateDaysBefore(departureDateTime); }

    private boolean arriveInTimeSlot(Planet otherPlanet) {
        return !(otherPlanet.arrivalDateTime.isBefore(arrivalDateTime)
                || otherPlanet.arrivalDateTime.isAfter(departureDateTime));
    }

    private boolean departInTimeSlot(Planet otherPlanet) {
        return !(otherPlanet.departureDateTime.isBefore(arrivalDateTime)
                || otherPlanet.departureDateTime.isAfter(departureDateTime));
    }

    public void addExcursion(Excursion excursion) {
        if (excursion.getStartDateTime().isBefore(this.arrivalDateTime)) {
            throw new InvalidExcursionException("Excursion starts before planet arrival.");
        }
        if (excursion.getStartDateTime().isAfter(this.departureDateTime)) {
            throw new InvalidExcursionException("Excursion starts after planet departure.");
        }
        var end = excursion.getStartDateTime().plusHours(excursion.getDurationHours());
        if (end.isAfter(this.departureDateTime)) {
            throw new InvalidExcursionException("Excursion ends after planet departure.");
        }
        excursion.setPlanet(this);
        excursions.add(excursion);
    }
}
