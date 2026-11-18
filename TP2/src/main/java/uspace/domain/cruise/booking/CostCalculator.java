package uspace.domain.cruise.booking;

import java.math.BigDecimal;
import uspace.domain.cruise.Cruise;

public interface CostCalculator {
    BigDecimal calculate(Booking booking, Cruise cruise);
}
