package uspace.domain.cruise.emergencyShuttles;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import uspace.domain.cruise.booking.traveler.TravelerId;
import uspace.domain.cruise.booking.traveler.TravelerName;

@Entity
public class ShuttleTraveler {
    @Id
    private TravelerId travelerId;

    private TravelerName travelerName;

    public  ShuttleTraveler() {}

    public ShuttleTraveler(TravelerId travelerId, TravelerName travelerName) {
        this.travelerId = travelerId;
        this.travelerName = travelerName;
    }

    public TravelerId getId() {
        return travelerId;
    }

    public TravelerName getName() {
        return travelerName;
    }
}
