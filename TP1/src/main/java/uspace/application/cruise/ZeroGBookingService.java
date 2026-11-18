package uspace.application.cruise;

import jakarta.inject.Inject;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;
import uspace.domain.cruise.booking.BookingId;
import uspace.domain.cruise.booking.traveler.TravelerId;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.exceptions.CruiseNotFoundException;

public class ZeroGBookingService {

    private final CruiseRepository cruises;

    @Inject
    public ZeroGBookingService(CruiseRepository cruises) {
        this.cruises = cruises;
    }

    public void bookZeroG(CruiseId cruiseId,
                          BookingId bookingId,
                          TravelerId travelerId,
                          CruiseDateTime experienceBookingDateTime) {

        Cruise cruise = cruises.findById(cruiseId);
        if (cruise == null) {
            throw new CruiseNotFoundException();
        }
        cruise.bookZeroGravityExperience(bookingId, travelerId, experienceBookingDateTime);
    }
}
