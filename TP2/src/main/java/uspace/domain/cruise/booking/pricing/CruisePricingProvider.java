package uspace.domain.cruise.booking.pricing;

import java.math.BigDecimal;
import uspace.domain.cruise.cabin.CabinType;

public interface CruisePricingProvider {
    BigDecimal basePrice(String cruiseId, CabinType cabinType);
    BigDecimal lateFeeMultiplier();
}
