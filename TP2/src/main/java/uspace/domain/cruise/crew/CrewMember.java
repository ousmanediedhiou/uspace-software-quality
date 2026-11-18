package uspace.domain.cruise.crew;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import uspace.domain.cruise.booking.traveler.Traveler;
import uspace.domain.cruise.booking.traveler.TravelerCategory;
import uspace.domain.cruise.booking.traveler.TravelerId;
import uspace.domain.cruise.booking.traveler.TravelerName;
import uspace.domain.cruise.booking.traveler.badge.Badge;

import java.util.List;

@Entity
@DiscriminatorValue("CREW_MEMBER")
public class CrewMember extends Traveler {

    @Column(name = "employeeId", nullable = true)
    private String employeeId;

    protected CrewMember() {}

    public CrewMember(TravelerId id, String employeeId, TravelerName name) {
        super(id, name, TravelerCategory.SENIOR, List.of());
        this.employeeId = employeeId;
    }

    public CrewMember(String employeeId, TravelerName name) {
        this(new TravelerId(), employeeId, name);
    }

    public String getEmployeeId() { return employeeId; }

    @Override
    public void bookZeroGravityExperience(
            uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience z,
            java.util.List<uspace.domain.cruise.booking.traveler.Traveler> all) {
    }
}
