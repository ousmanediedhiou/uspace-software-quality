package uspace.domain.cruise.sport;

import uspace.domain.cruise.booking.traveler.TravelerCategory;

public class Sport {
    private final String name;
    private final SportAudience audience;

    public Sport(String name, SportAudience audience) {
        this.name = name;
        this.audience = audience;
    }

    public String getName() {
        return name;
    }

    public SportAudience getAudience() {
        return audience;
    }

    public boolean isBookableFor(TravelerCategory travelerCategory) {
        switch (audience) {
            case ALL:
                return travelerCategory == TravelerCategory.ADULT
                        || travelerCategory == TravelerCategory.CHILD
                        || travelerCategory == TravelerCategory.SENIOR;
            case ADULT:
                return travelerCategory == TravelerCategory.ADULT;
            case CHILD:
                return travelerCategory == TravelerCategory.CHILD;
            case SENIOR:
                return travelerCategory == TravelerCategory.SENIOR;
            case TEEN:
                return false;
            default:
                return false;
        }
    }
}
