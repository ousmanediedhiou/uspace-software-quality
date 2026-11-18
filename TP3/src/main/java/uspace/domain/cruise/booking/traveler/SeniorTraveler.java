package uspace.domain.cruise.booking.traveler;

import jakarta.persistence.Entity;
import uspace.domain.cruise.booking.traveler.badge.Badge;
import uspace.domain.cruise.sport.Sport;
import uspace.domain.cruise.sport.exceptions.SportNotBookableException;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;

import java.util.List;
@Entity
public class SeniorTraveler extends Traveler {

    public SeniorTraveler() {
    }

    public SeniorTraveler(TravelerId id, TravelerName name, List<Badge> badges, StandardCostMultiplier standardCostMultiplier) {
        super(id, name, TravelerCategory.SENIOR, badges, standardCostMultiplier);
    }

    @Override
    public void bookZeroGravityExperience(ZeroGravityExperience zeroGravityExperience, List<Traveler> travelersInBooking) {
        zeroGravityExperience.book(id);
        earnOneTimeBadge(Badge.ZERO_G);
        earnOneTimeBadge(Badge.STILL_GOT_IT);
    }

    @Override
    public boolean canAccompagnyChildForZeroGravityExperience(ZeroGravityExperience zeroGravityExperience) {
        return zeroGravityExperience.hasTravelerBooked(id);
    }

    @Override
    public void bookSport(Sport sport) {
        if (!sport.isBookableFor(getCategory())) {
            throw new SportNotBookableException();
        }
        bookSportInternal(sport);
        if (!getBadges().contains(Badge.SPORTY)) {
            earnOneTimeBadge(Badge.SPORTY);
        } else if (!getBadges().contains(Badge.SUPER_SPORTY)) {
            earnOneTimeBadge(Badge.SUPER_SPORTY);
        }
    }
}
