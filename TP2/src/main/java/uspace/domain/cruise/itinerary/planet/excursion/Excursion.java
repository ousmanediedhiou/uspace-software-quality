package uspace.domain.cruise.itinerary.planet.excursion;

import jakarta.persistence.*;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.itinerary.planet.Planet;

@Entity
@Table(name = "excursion")
public class Excursion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Embedded
    private CruiseDateTime startDateTime;

    private int durationHours;
    private int capacity;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "planet_name", referencedColumnName = "name", nullable = false)
    private Planet planet;

    protected Excursion() { }

    public Excursion(String name, String description, CruiseDateTime startDateTime, int durationHours, int capacity) {
        this.name = name;
        this.description = description;
        this.startDateTime = startDateTime;
        this.durationHours = durationHours;
        this.capacity = capacity;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public CruiseDateTime getStartDateTime() { return startDateTime; }
    public int getDurationHours() { return durationHours; }
    public int getCapacity() { return capacity; }
    public Planet getPlanet() { return planet; }
    public void setPlanet(Planet planet) { this.planet = planet; }
}
