package uspace.domain.cruise.booking.traveler;

import java.util.ArrayList;
import java.util.List;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import uspace.domain.cruise.booking.traveler.badge.Badge;

@Singleton
public class TravelerFactory {

    @Inject
    public TravelerFactory() {
    }

    public Traveler create(String travelerNameStr, TravelerCategory travelerCategory) {
        TravelerId travelerId = new TravelerId();
        TravelerName travelerName = new TravelerName(travelerNameStr);
        List<Badge> badges = new ArrayList<>();

        switch (travelerCategory) {
            case ADULT:
                return new AdultTraveler(travelerId, travelerName, badges);
            case SENIOR:
                return new SeniorTraveler(travelerId, travelerName, badges);
            case CHILD:
            default:
                return new ChildTraveler(travelerId, travelerName, badges);
        }
    }
}
