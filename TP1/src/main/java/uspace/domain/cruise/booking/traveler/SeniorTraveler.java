package uspace.domain.cruise.booking.traveler;

import java.util.List;
import uspace.domain.cruise.booking.traveler.badge.Badge;

public final class SeniorTraveler extends Traveler {
    public SeniorTraveler(TravelerId id, TravelerName name, List<Badge> badges) {
        super(id, name, TravelerCategory.SENIOR, badges);
    }

    @Override
    protected void validateZeroGEligibility(List<Traveler> bookingTravelers) {
    }

    @Override
    protected void grantZeroGBadges() {
        earnBadge(Badge.ZERO_G);
        earnBadge(Badge.STILL_GOT_IT);
    }
}
