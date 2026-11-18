package uspace.domain.cruise.booking.traveler;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import uspace.domain.cruise.booking.traveler.badge.Badge;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("SENIOR")
public class SeniorTraveler extends Traveler {

    private boolean earnedZeroG;
    private boolean earnedStillGotIt;

    protected SeniorTraveler() {}

    public SeniorTraveler(TravelerId id, TravelerName name, List<Badge> badges) {
        super(id, name, TravelerCategory.SENIOR, badges);
        if (badges != null) {
            this.earnedZeroG = badges.contains(Badge.ZERO_G);
            this.earnedStillGotIt = badges.contains(Badge.STILL_GOT_IT);
        }
    }

    @Override
    public void bookZeroGravityExperience(
            uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience z,
            List<Traveler> all) {
        z.book(getId());
        if (!earnedZeroG) {
            earnedZeroG = true;
            addBadge(Badge.ZERO_G);
        } else if (!earnedStillGotIt) {
            earnedStillGotIt = true;
            addBadge(Badge.STILL_GOT_IT);
        }
    }

    @Override
    public List<Badge> getBadges() {
        List<Badge> r = new ArrayList<>(super.getBadges());
        return r;
    }
}
