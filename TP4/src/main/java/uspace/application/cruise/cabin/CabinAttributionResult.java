package uspace.application.cruise.cabin;

import uspace.domain.cruise.cabin.CabinType;

public class CabinAttributionResult {

    private final String cabinId;
    private final String bookingId;
    private final CabinType cabinType;

    public CabinAttributionResult(String cabinId, String bookingId, CabinType cabinType) {
        this.cabinId = cabinId;
        this.bookingId = bookingId;
        this.cabinType = cabinType;
    }

    public String getCabinId() {
        return cabinId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public CabinType getCabinType() {
        return cabinType;
    }
}
