package uspace.application.cruise.booking.dtos;

import java.math.BigDecimal;
import java.util.List;

public class BookingDto {

    public String cruiseId;
    public String bookingId;
    public List<BookingTravelerDto> travelers;
    public String cabinType;
    public String bookingDateTime;
    public BigDecimal cost;

    public BookingDto(String cruiseId, String bookingId, List<BookingTravelerDto> travelers,
                      String cabinType, String bookingDateTime, BigDecimal cost) {
        this.cruiseId = cruiseId;
        this.bookingId = bookingId;
        this.travelers = travelers;
        this.cabinType = cabinType;
        this.bookingDateTime = bookingDateTime;
        this.cost = cost;
    }
}
