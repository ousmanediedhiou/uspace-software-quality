package uspace.domain.cruise.booking.traveler;

import java.util.List;
import java.util.Objects;

import uspace.domain.cruise.booking.traveler.badge.Badge;
import uspace.domain.cruise.booking.traveler.exceptions.ZeroGravityExperienceChildCriteriaException;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;

public abstract class Traveler {
    private final TravelerId id;
    private final TravelerName name;
    private final TravelerCategory category;
    private final List<Badge> badges;

    protected Traveler(TravelerId id, TravelerName name, TravelerCategory category, List<Badge> badges) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.category = Objects.requireNonNull(category);
        this.badges = Objects.requireNonNull(badges);
    }

    public TravelerId getId() { return id; }
    public TravelerName getName() { return name; }
    public TravelerCategory getCategory() { return category; }
    public List<Badge> getBadges() { return badges; }

    public final void bookZeroGravityExperience(ZeroGravityExperience zeroGravityExperience,
                                                List<Traveler> bookingTravelers) {
        Objects.requireNonNull(zeroGravityExperience);
        Objects.requireNonNull(bookingTravelers);

        validateZeroGEligibility(bookingTravelers);
        zeroGravityExperience.book(id);
        grantZeroGBadges();
    }
    public void bookZeroGravityExperience(ZeroGravityExperience zeroGravityExperience) {
        bookZeroGravityExperience(zeroGravityExperience, List.of(this));
    }

    protected void earnBadge(Badge badge) {
        if (!badges.contains(badge)) {
            badges.add(badge);
        }
    }

    protected abstract void validateZeroGEligibility(List<Traveler> bookingTravelers)
            throws ZeroGravityExperienceChildCriteriaException;

    protected abstract void grantZeroGBadges();
}
