package uspace.infra.pricing;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import jakarta.inject.Singleton;
import uspace.domain.cruise.booking.pricing.CruisePricingProvider;
import uspace.domain.cruise.cabin.CabinType;

@Singleton
public class InMemoryCruisePricingProvider implements CruisePricingProvider {

    private static final BigDecimal LATE_FEE = new BigDecimal("1.5");
    private final Map<String, Map<CabinType, BigDecimal>> pricesByCruise = new HashMap<>();

    public InMemoryCruisePricingProvider() {
        Map<CabinType, BigDecimal> jupiter = new EnumMap<>(CabinType.class);
        jupiter.put(CabinType.STANDARD, new BigDecimal("1000.00"));
        jupiter.put(CabinType.DELUXE,   new BigDecimal("1999.99"));
        jupiter.put(CabinType.SUITE,    new BigDecimal("4500.00"));
        pricesByCruise.put("JUPITER_MOONS_EXPLORATION_2085", jupiter);
    }

    @Override
    public BigDecimal basePrice(String cruiseId, CabinType cabinType) {
        return pricesByCruise.get(cruiseId).get(cabinType);
    }

    @Override
    public BigDecimal lateFeeMultiplier() {
        return LATE_FEE;
    }
}
