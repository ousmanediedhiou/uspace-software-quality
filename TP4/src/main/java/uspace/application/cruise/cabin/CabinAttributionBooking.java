package uspace.application.cruise.cabin;

import uspace.domain.cruise.booking.BookingId;
import uspace.domain.cruise.cabin.CabinType;
import uspace.domain.cruise.dateTime.CruiseDateTime;

public class CabinAttributionBooking {

    private final BookingId bookingId;
    private final CabinType cabinType;
    private final int travelerCount;
    private final CruiseDateTime bookingDateTime;

    public CabinAttributionBooking(BookingId bookingId,
                                   CabinType cabinType,
                                   int travelerCount,
                                   CruiseDateTime bookingDateTime) {
        this.bookingId = bookingId;
        this.cabinType = cabinType;
        this.travelerCount = travelerCount;
        this.bookingDateTime = bookingDateTime;
    }

    public BookingId getBookingId() {
        return bookingId;
    }

    public CabinType getCabinType() {
        return cabinType;
    }

    public int getTravelerCount() {
        return travelerCount;
    }

    public CruiseDateTime getBookingDateTime() {
        return bookingDateTime;
    }
}
