package uspace.domain.cruise.emergencyShuttles;

import jakarta.persistence.*;
import uspace.domain.cruise.booking.traveler.Traveler;
import uspace.domain.cruise.crew.crewMember.CrewMember;
import uspace.domain.cruise.money.Money;

import java.util.List;

@Entity
public class Shuttle {

    @Id
    private ShuttleId id;

    private Money cost;
    private ShuttleType type;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "SHUTTLE_TRAVELER_ASSIGNMENT", // Nom de la nouvelle table de jointure
            joinColumns = @JoinColumn(name = "shuttle_id"),
            inverseJoinColumns = @JoinColumn(name = "shuttle_traveler_id")
    )
    private List<ShuttleTraveler> travelers;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "SHUTTLE_CREW_ASSIGNMENT", // Nom de la nouvelle table de jointure
            joinColumns = @JoinColumn(name = "shuttle_id"), // Clé du Shuttle
            inverseJoinColumns = @JoinColumn(name = "shuttle_crew_member_id") // Clé du ShuttleCrewMember
    )
    private List<ShuttleCrewMember> crewMembers;

    private Integer capacity;

    public Shuttle(){}

    public Shuttle(Money cost, ShuttleType type, List<ShuttleTraveler> travelers, List<ShuttleCrewMember> crewMembers, Integer capacity) {
        this.id = new ShuttleId();
        this.cost = cost;
        this.type = type;
        this.travelers = travelers;
        this.crewMembers = crewMembers;
        this.capacity = capacity;
    }

    public Money getCost() {
        return cost;
    }

    public ShuttleType getType() {
        return type;
    }

    public List<ShuttleTraveler> getTravelers() {
        return travelers;
    }

    public List<ShuttleCrewMember> getCrewMembers() {
        return crewMembers;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public boolean isFull(){
        return (travelers.size() + crewMembers.size()) == capacity;
    }

    public void addTraveler(Traveler traveler) {
        ShuttleTraveler newShuttleTraveler = new ShuttleTraveler(traveler.getId(), traveler.getName());
        travelers.add(newShuttleTraveler);
    }

    public void addCrewMember(CrewMember crewMember) {
        ShuttleCrewMember newShuttleCrewMember = new ShuttleCrewMember(crewMember.getEmployeeId(), crewMember.getName());
        crewMembers.add(newShuttleCrewMember);
    }
}
