package uspace.domain.cruise.booking;

import jakarta.persistence.*;
import uspace.domain.cruise.booking.exceptions.InvalidBookingDateException;
import uspace.domain.cruise.booking.traveler.Traveler;
import uspace.domain.cruise.booking.traveler.TravelerId;
import uspace.domain.cruise.cabin.CabinType;
import uspace.domain.cruise.dateTime.CruiseDateTime;
import uspace.domain.cruise.exceptions.NoTravelerToBookException;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;

import java.math.BigDecimal;
import java.util.List;

@Entity
public class Booking {

    @Id
    private BookingId id;

    @OneToMany(mappedBy = "booking", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Traveler> travelers;

    @Enumerated(EnumType.STRING)
    private CabinType cabinType;

    @Embedded
    private CruiseDateTime bookingDateTime;

    private BigDecimal cost;

    protected Booking() {}

    public Booking(BookingId id, List<Traveler> travelers, CabinType cabinType, CruiseDateTime bookingDateTime) {
        this.id = id;
        this.travelers = travelers;
        this.cabinType = cabinType;
        this.bookingDateTime = bookingDateTime;
        if (this.travelers != null) {
            for (Traveler t : this.travelers) {
                t.setBooking(this);
            }
        }
    }

    public BookingId getId() { return id; }
    public CabinType getCabinType() { return cabinType; }
    public List<Traveler> getTravelers() { return travelers; }
    public CruiseDateTime getBookingDateTime() { return bookingDateTime; }
    public BigDecimal getCost() { return cost; }
    public void setCost(BigDecimal cost) { this.cost = cost; }

    public void validate(CruiseDateTime cruiseDepartureDateTime) {
        if (travelers == null || travelers.isEmpty()) throw new NoTravelerToBookException();
        if (!bookingDateTime.isBefore(cruiseDepartureDateTime)) throw new InvalidBookingDateException();
    }

    public int getTravelersNumber() { return travelers.size(); }

    public void bookZeroGravityExperience(TravelerId travelerId, ZeroGravityExperience zeroGravityExperience) {
        Traveler traveler = findTravelerById(travelerId);
        if (traveler == null)
            throw new uspace.domain.cruise.booking.traveler.exceptions.TravelerNotFoundException();
        traveler.bookZeroGravityExperience(zeroGravityExperience, travelers);
    }

    private Traveler findTravelerById(TravelerId travelerId) {
        return travelers.stream().filter(t -> t.getId().equals(travelerId)).findFirst().orElse(null);
    }
}
