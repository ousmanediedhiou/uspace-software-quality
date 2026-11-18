package uspace.domain.cruise.booking;

import java.math.BigDecimal;
import java.math.RoundingMode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.booking.traveler.Traveler;
import uspace.domain.cruise.booking.traveler.TravelerCategory;
import uspace.domain.cruise.booking.pricing.CruisePricingProvider;

@Singleton
public class BookingCostCalculator implements CostCalculator {

    private static final long EARLY_BOOKING_LIMIT_DAYS = 7;
    private final CruisePricingProvider pricing;

    @Inject
    public BookingCostCalculator(CruisePricingProvider pricing) {
        this.pricing = pricing;
    }

    @Override
    public BigDecimal calculate(Booking booking, Cruise cruise) {
        BigDecimal base = pricing.basePrice(cruise.getId().toString(), booking.getCabinType());
        long daysBefore = booking.getBookingDateTime().calculateDaysBefore(cruise.getDepartureDateTime());
        BigDecimal total = BigDecimal.ZERO;
        if (daysBefore >= EARLY_BOOKING_LIMIT_DAYS) {
            for (Traveler t : booking.getTravelers()) {
                BigDecimal price = t.getCategory() == TravelerCategory.CHILD
                        ? base.multiply(new BigDecimal("0.5"))
                        : base;
                total = total.add(price);
            }
        } else {
            total = base.multiply(BigDecimal.valueOf(booking.getTravelersNumber()))
                    .multiply(pricing.lateFeeMultiplier());
        }
        return total.setScale(2, RoundingMode.HALF_EVEN);
    }
}
