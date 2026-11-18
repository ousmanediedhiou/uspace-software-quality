package uspace.domain.cruise;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import uspace.application.cruise.crew.dtos.CrewMemberDto;
import uspace.domain.cruise.booking.Booking;
import uspace.domain.cruise.booking.BookingId;
import uspace.domain.cruise.booking.CruiseBookings;
import uspace.domain.cruise.booking.traveler.TravelerId;
import uspace.domain.cruise.booking.traveler.TravelerName;
import uspace.domain.cruise.booking.traveler.exceptions.TravelerNotFoundException;
import uspace.domain.cruise.cabin.CabinAvailabilities;
import uspace.domain.cruise.cabin.CabinType;
import uspace.domain.cruise.crew.CrewMember;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.itinerary.Itinerary;
import uspace.domain.cruise.itinerary.exceptions.InvalidPlanetDateException;
import uspace.domain.cruise.itinerary.planet.Planet;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;
import uspace.domain.cruise.zeroGravityExperience.exceptions.ZeroGravityExperienceBookingTimeException;
import uspace.domain.cruise.itinerary.planet.excursion.Excursion;
import uspace.domain.cruise.itinerary.planet.excursion.exceptions.ExcursionAlreadyExistsException;
import uspace.domain.cruise.itinerary.exceptions.InvalidPlanetException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "Cruise")
public class Cruise {
    @EmbeddedId
    private CruiseId id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "localDateTime", column = @Column(name = "departureDateTime"))
    })
    private CruiseDateTime departureDateTime;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "localDateTime", column = @Column(name = "endDateTime"))
    })
    private CruiseDateTime endDateTime;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    @Fetch(FetchMode.SELECT)
    private CabinAvailabilities cabinAvailabilities;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    @Fetch(FetchMode.SELECT)
    private CruiseBookings cruiseBookings;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    @Fetch(FetchMode.SELECT)
    private ZeroGravityExperience zeroGravityExperience;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    @Fetch(FetchMode.SELECT)
    private Itinerary itinerary;

    @OneToMany(mappedBy = "cruise", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKey(name = "employeeId")
    private Map<String, CrewMember> crewMembers = new HashMap<>();

    protected Cruise() { }

    public Cruise(CruiseId id, CruiseDateTime departureDateTime, CruiseDateTime endDateTime, CabinAvailabilities cabinAvailabilities,
                  CruiseBookings cruiseBookings, ZeroGravityExperience zeroGravityExperience, Itinerary itinerary) {
        this.id = id;
        this.departureDateTime = departureDateTime;
        this.endDateTime = endDateTime;
        this.cabinAvailabilities = cabinAvailabilities;
        this.cruiseBookings = cruiseBookings;
        this.zeroGravityExperience = zeroGravityExperience;
        this.itinerary = itinerary;
    }

    public CruiseId getId() { return id; }
    public CruiseDateTime getDepartureDateTime() { return departureDateTime; }
    public CruiseDateTime getEndDateTime() { return endDateTime; }
    public List<Planet> getPlanetsInItinerary() { return itinerary.getPlanets(); }

    public void processBooking(Booking booking) {
        booking.validate(departureDateTime);
        cabinAvailabilities.bookCabin(booking.getCabinType());
        cruiseBookings.add(booking);
    }

    public Booking findBookingById(BookingId bookingId) {
        return cruiseBookings.findById(bookingId);
    }

    public void bookZeroGravityExperience(BookingId bookingId, TravelerId travelerId, CruiseDateTime experienceBookingDateTime) {
        if (experienceBookingDateTime.isAfter(departureDateTime)) {
            throw new ZeroGravityExperienceBookingTimeException();
        }
        Booking booking = cruiseBookings.findById(bookingId);
        if (booking == null) {
            throw new TravelerNotFoundException();
        }
        booking.bookZeroGravityExperience(travelerId, zeroGravityExperience);
    }

    public void addPlanetToItinerary(Planet planet) {
        if (planet.arriveBefore(departureDateTime) || planet.departAfter(endDateTime)) {
            throw new InvalidPlanetDateException();
        }
        itinerary.addPlanet(planet);
    }

    @Deprecated
    public void addCrewMember(uspace.application.cruise.crew.dtos.CrewMemberDto newCrewMemberDto) {
        CrewMember newCrewMember = new CrewMember(newCrewMemberDto.employeeId, new TravelerName(newCrewMemberDto.name));
        addCrewMember(newCrewMember);
    }

    public List<CrewMember> getCrewMembers() {
        return List.copyOf(crewMembers.values());
    }

    public void bookCabin(CabinType cabinType) {
        cabinAvailabilities.bookCabin(cabinType);
    }

    public void addCrewMember(CrewMember newCrewMember) {
        if (crewMembers.get(newCrewMember.getEmployeeId()) != null) {
            throw new uspace.domain.cruise.crew.exceptions.CrewMemberAlreadyInCruiseException();
        }
        newCrewMember.setCruise(this);
        crewMembers.put(newCrewMember.getEmployeeId(), newCrewMember);
    }

    public void addExcursionToPlanet(String planetName, Excursion excursion) {
        var planet = getPlanetsInItinerary().stream()
                .filter(p -> p.getName().toString().equalsIgnoreCase(planetName))
                .findFirst()
                .orElseThrow(InvalidPlanetException::new);
        boolean nameAlreadyUsed = getPlanetsInItinerary().stream()
                .flatMap(p -> p.getExcursions().stream())
                .anyMatch(e -> e.getName().equalsIgnoreCase(excursion.getName()));
        if (nameAlreadyUsed) {
            throw new ExcursionAlreadyExistsException();
        }
        planet.addExcursion(excursion);
    }
}
