package uspace.domain.cruise.booking.traveler;

import java.util.List;
import uspace.domain.cruise.booking.traveler.badge.Badge;
import uspace.domain.cruise.booking.traveler.exceptions.ZeroGravityExperienceChildCriteriaException;

public final class ChildTraveler extends Traveler {
    public ChildTraveler(TravelerId id, TravelerName name, List<Badge> badges) {
        super(id, name, TravelerCategory.CHILD, badges);
    }

    @Override
    protected void validateZeroGEligibility(List<Traveler> bookingTravelers) {
        boolean allowed = bookingTravelers.stream().anyMatch(t ->
                (t.getCategory() == TravelerCategory.ADULT || t.getCategory() == TravelerCategory.SENIOR)
                        && (t.getBadges().contains(Badge.ZERO_G) || t.getBadges().contains(Badge.STILL_GOT_IT))
        );
        if (!allowed) {
            throw new ZeroGravityExperienceChildCriteriaException(
                    "Child requires an ADULT or SENIOR in the same booking who already booked."
            );
        }
    }

    @Override
    protected void grantZeroGBadges() {
        earnBadge(Badge.MINI_ZERO_G);
    }
}
