package uspace.domain.cruise.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.booking.pricing.CruisePricingProvider;
import uspace.domain.cruise.booking.traveler.Traveler;
import uspace.domain.cruise.booking.traveler.TravelerCategory;
import uspace.domain.cruise.cabin.CabinType;
import uspace.domain.cruise.dateTime.CruiseDateTime;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingCostCalculatorTest {

    private static final String CRUISE_CODE = "JUPITER_MOONS_EXPLORATION_2085";
    private static final CruiseId CRUISE_ID = new CruiseId(CRUISE_CODE);

    private static final int DAYS_BEFORE_EARLY = 8;
    private static final int DAYS_BEFORE_LATE = 3;
    private static final int DAYS_BEFORE_EXACT = 7;
    private static final int DAYS_BEFORE_FAR = 15;

    private static final int TWO_TRAVELERS = 2;
    private static final int THREE_TRAVELERS = 3;

    private static final LocalDateTime DEPARTURE_LOCAL_DATE_TIME = LocalDateTime.of(2085, 1, 25, 12, 0);
    private static final CruiseDateTime DEPARTURE_DATE_TIME = new CruiseDateTime(DEPARTURE_LOCAL_DATE_TIME);
    private static final CruiseDateTime BOOKING_DATE_EARLY  = new CruiseDateTime(DEPARTURE_LOCAL_DATE_TIME.minusDays(DAYS_BEFORE_EARLY));
    private static final CruiseDateTime BOOKING_DATE_LATE   = new CruiseDateTime(DEPARTURE_LOCAL_DATE_TIME.minusDays(DAYS_BEFORE_LATE));
    private static final CruiseDateTime BOOKING_DATE_EXACT7 = new CruiseDateTime(DEPARTURE_LOCAL_DATE_TIME.minusDays(DAYS_BEFORE_EXACT));
    private static final CruiseDateTime BOOKING_DATE_FAR    = new CruiseDateTime(DEPARTURE_LOCAL_DATE_TIME.minusDays(DAYS_BEFORE_FAR));

    private static final BigDecimal PRICE_STANDARD = new BigDecimal("1000.00");
    private static final BigDecimal PRICE_DELUXE   = new BigDecimal("1999.99");
    private static final BigDecimal PRICE_SUITE    = new BigDecimal("4500.00");
    private static final BigDecimal LATE_FEE_MULTIPLIER = new BigDecimal("1.5");

    private static final BigDecimal EXPECTED_DELUXE_ADULT_CHILD_EARLY = new BigDecimal("2999.98");
    private static final BigDecimal EXPECTED_DELUXE_TWO_PAX_LATE      = new BigDecimal("5999.97");
    private static final BigDecimal EXPECTED_STANDARD_2ADULT_1CH_EX7  = new BigDecimal("2500.00");
    private static final BigDecimal EXPECTED_SUITE_1ADULT_EARLY       = new BigDecimal("4500.00");
    private static final BigDecimal EXPECTED_DELUXE_3ADULTS_EARLY     = new BigDecimal("5999.97");

    @Mock private Cruise cruise;
    @Mock private Booking booking;
    @Mock private Traveler adult1;
    @Mock private Traveler adult2;
    @Mock private Traveler adult3;
    @Mock private Traveler child1;
    @Mock private CruisePricingProvider pricing;

    private BookingCostCalculator calculator;

    @BeforeEach
    void setup() {
        calculator = new BookingCostCalculator(pricing);
        when(cruise.getDepartureDateTime()).thenReturn(DEPARTURE_DATE_TIME);
        when(cruise.getId()).thenReturn(CRUISE_ID);
    }

    @Test
    void givenDeluxeOneAdultOneChildEarlyBooking_whenCalculate_thenExpectedAmount() {
        when(booking.getBookingDateTime()).thenReturn(BOOKING_DATE_EARLY);
        when(booking.getCabinType()).thenReturn(CabinType.DELUXE);
        when(adult1.getCategory()).thenReturn(TravelerCategory.ADULT);
        when(child1.getCategory()).thenReturn(TravelerCategory.CHILD);
        when(booking.getTravelers()).thenReturn(List.of(adult1, child1));
        when(pricing.basePrice(CRUISE_CODE, CabinType.DELUXE)).thenReturn(PRICE_DELUXE);

        BigDecimal cost = calculator.calculate(booking, cruise);

        assertEquals(EXPECTED_DELUXE_ADULT_CHILD_EARLY, cost);
    }

    @Test
    void givenDeluxeOneAdultOneChildLateBooking_whenCalculate_thenExpectedAmount() {
        when(booking.getBookingDateTime()).thenReturn(BOOKING_DATE_LATE);
        when(booking.getCabinType()).thenReturn(CabinType.DELUXE);
        when(booking.getTravelersNumber()).thenReturn(TWO_TRAVELERS);
        when(pricing.basePrice(CRUISE_CODE, CabinType.DELUXE)).thenReturn(PRICE_DELUXE);
        when(pricing.lateFeeMultiplier()).thenReturn(LATE_FEE_MULTIPLIER);

        BigDecimal cost = calculator.calculate(booking, cruise);

        assertEquals(EXPECTED_DELUXE_TWO_PAX_LATE, cost);
    }

    @Test
    void givenStandardTwoAdultsOneChildExactly7Days_whenCalculate_thenExpectedAmount() {
        when(booking.getBookingDateTime()).thenReturn(BOOKING_DATE_EXACT7);
        when(booking.getCabinType()).thenReturn(CabinType.STANDARD);
        when(adult1.getCategory()).thenReturn(TravelerCategory.ADULT);
        when(adult2.getCategory()).thenReturn(TravelerCategory.ADULT);
        when(child1.getCategory()).thenReturn(TravelerCategory.CHILD);
        when(booking.getTravelers()).thenReturn(List.of(adult1, adult2, child1));
        when(pricing.basePrice(CRUISE_CODE, CabinType.STANDARD)).thenReturn(PRICE_STANDARD);

        BigDecimal cost = calculator.calculate(booking, cruise);

        assertEquals(EXPECTED_STANDARD_2ADULT_1CH_EX7, cost);
    }

    @Test
    void givenSuiteOneAdultEarlyBooking_whenCalculate_thenExpectedAmount() {
        when(booking.getBookingDateTime()).thenReturn(BOOKING_DATE_FAR);
        when(booking.getCabinType()).thenReturn(CabinType.SUITE);
        when(adult1.getCategory()).thenReturn(TravelerCategory.ADULT);
        when(booking.getTravelers()).thenReturn(List.of(adult1));
        when(pricing.basePrice(CRUISE_CODE, CabinType.SUITE)).thenReturn(PRICE_SUITE);

        BigDecimal cost = calculator.calculate(booking, cruise);

        assertEquals(EXPECTED_SUITE_1ADULT_EARLY, cost);
    }

    @Test
    void givenDeluxeThreeAdultsEarlyBooking_whenCalculate_thenExpectedAmount() {
        when(booking.getBookingDateTime()).thenReturn(BOOKING_DATE_EARLY);
        when(booking.getCabinType()).thenReturn(CabinType.DELUXE);
        when(adult1.getCategory()).thenReturn(TravelerCategory.ADULT);
        when(adult2.getCategory()).thenReturn(TravelerCategory.ADULT);
        when(adult3.getCategory()).thenReturn(TravelerCategory.ADULT);
        when(booking.getTravelers()).thenReturn(List.of(adult1, adult2, adult3));
        when(pricing.basePrice(CRUISE_CODE, CabinType.DELUXE)).thenReturn(PRICE_DELUXE);

        BigDecimal cost = calculator.calculate(booking, cruise);

        assertEquals(EXPECTED_DELUXE_3ADULTS_EARLY, cost);
    }
}
