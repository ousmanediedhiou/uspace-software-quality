package uspace.domain.cruise.booking.traveler;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import uspace.domain.cruise.booking.traveler.badge.Badge;

import java.util.List;

@Entity
@DiscriminatorValue("CHILD")
public class ChildTraveler extends Traveler {

    protected ChildTraveler() {}

    public ChildTraveler(TravelerId id, TravelerName name, List<Badge> badges) {
        super(id, name, TravelerCategory.CHILD, badges);
    }

    @Override
    public void bookZeroGravityExperience(
            uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience z,
            List<Traveler> all) {
        z.book(getId());
    }
}
