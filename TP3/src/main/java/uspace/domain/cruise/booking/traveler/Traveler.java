package uspace.domain.cruise.booking.traveler;

import jakarta.persistence.*;
import uspace.domain.cruise.booking.traveler.badge.Badge;
import uspace.domain.cruise.money.Money;
import uspace.domain.cruise.sport.Sport;
import uspace.domain.cruise.sport.exceptions.SportAlreadyBookedException;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Traveler {

    @Id
    protected TravelerId id;

    private TravelerName name;

    @Enumerated(EnumType.STRING)
    private TravelerCategory category;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Badge> badges;

    private StandardCostMultiplier standardCostMultiplier;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> bookedSports;

    protected Traveler() {

    }

    protected Traveler(TravelerId id, TravelerName name, TravelerCategory category, List<Badge> badges, StandardCostMultiplier standardCostMultiplier)
    {
        this.id = id;
        this.name = name;
        this.category = category;
        this.badges = badges;
        this.standardCostMultiplier = standardCostMultiplier;
    }

    public TravelerId getId() {
        return id;
    }

    public TravelerName getName() {
        return name;
    }

    public TravelerCategory getCategory() {
        return category;
    }

    public List<Badge> getBadges() { return badges; }

    public List<String> getBookedSports() {
        return bookedSports;
    }

    public abstract void bookZeroGravityExperience(ZeroGravityExperience zeroGravityExperience,
                                                   List<Traveler> travelersInBooking);

    public abstract boolean canAccompagnyChildForZeroGravityExperience(ZeroGravityExperience zeroGravityExperience);

    public abstract void bookSport(Sport sport);

    public Money calculateStandardBookingCost(Money cabinBaseCostPerPerson) {
        return standardCostMultiplier.applyToMoney(cabinBaseCostPerPerson);
    }

    protected void earnOneTimeBadge(Badge badge) {
        if (!badges.contains(badge))
        {
            badges.add(badge);
        }
    }

    protected void bookSportInternal(Sport sport) {
        if (bookedSports == null) {
            bookedSports = new ArrayList<>();
        }
        if (bookedSports.contains(sport.getName())) {
            throw new SportAlreadyBookedException();
        }
        bookedSports.add(sport.getName());
    }

}
