package uspace.domain.cruise.booking.traveler;

import jakarta.persistence.Entity;
import uspace.domain.cruise.booking.traveler.badge.Badge;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;

import java.util.List;
import uspace.domain.cruise.sport.Sport;
import uspace.domain.cruise.sport.exceptions.SportNotBookableException;


@Entity
public class AdultTraveler extends Traveler{

    public AdultTraveler() {
    }

    public AdultTraveler(TravelerId id, TravelerName name, List<Badge> badges, StandardCostMultiplier standardCostMultiplier) {
        super(id, name, TravelerCategory.ADULT, badges, standardCostMultiplier);
    }

    @Override
    public void bookZeroGravityExperience(ZeroGravityExperience zeroGravityExperience, List<Traveler> travelersInBooking) {
        zeroGravityExperience.book(id);
        earnOneTimeBadge(Badge.ZERO_G);
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
        earnOneTimeBadge(Badge.SPORTY);
    }
}
