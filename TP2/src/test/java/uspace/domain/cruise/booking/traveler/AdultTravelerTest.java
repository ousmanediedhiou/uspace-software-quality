package uspace.domain.cruise.booking.traveler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.domain.cruise.booking.traveler.badge.Badge;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AdultTravelerTest {

    private static final TravelerId ANY_ID = new TravelerId();
    private static final TravelerName ANY_NAME = new TravelerName("Bob");
    private static final List<Badge> NO_BADGES = new ArrayList<>();
    private static final List<Badge> WITH_ZERO_G = List.of(Badge.ZERO_G);
    private static final List<Traveler> EMPTY_BOOKING = List.of();
    private static final TravelerCategory EXPECTED_CATEGORY = TravelerCategory.ADULT;
    private static final int ONE = 1;

    @Mock
    private ZeroGravityExperience zeroGravity;

    private AdultTraveler sut;

    @BeforeEach
    void setUp() {
        sut = new AdultTraveler(ANY_ID, ANY_NAME, new ArrayList<>(NO_BADGES));
    }

    @Test
    void whenCreate_thenCategoryIsAdult() {
        TravelerCategory actual = sut.getCategory();
        assertEquals(EXPECTED_CATEGORY, actual);
    }

    @Test
    void whenBookZeroG_thenExperienceIsBookedWithTravelerId() {
        sut.bookZeroGravityExperience(zeroGravity, EMPTY_BOOKING);
        verify(zeroGravity).book(ANY_ID);
    }

    @Test
    void givenAlreadyHasZeroG_whenBookZeroG_thenNoDuplicateBadge() {
        AdultTraveler withBadge = new AdultTraveler(ANY_ID, ANY_NAME, new ArrayList<>(WITH_ZERO_G));

        withBadge.bookZeroGravityExperience(zeroGravity, EMPTY_BOOKING);

        int count = (int) withBadge.getBadges().stream().filter(Badge.ZERO_G::equals).count();
        assertEquals(ONE, count);
    }
}
