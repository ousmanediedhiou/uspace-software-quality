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
class SeniorTravelerTest {

    private static final TravelerId ANY_ID = new TravelerId();
    private static final TravelerName ANY_NAME = new TravelerName("Alice");
    private static final List<Badge> NO_BADGES = new ArrayList<>();
    private static final List<Badge> WITH_STILL_GOT_IT = List.of(Badge.STILL_GOT_IT);
    private static final List<Traveler> EMPTY_BOOKING = List.of();
    private static final TravelerCategory EXPECTED_CATEGORY = TravelerCategory.SENIOR;
    private static final int ONE = 1;

    @Mock
    private ZeroGravityExperience zeroGravity;

    private SeniorTraveler sut;

    @BeforeEach
    void setUp() {
        sut = new SeniorTraveler(ANY_ID, ANY_NAME, new ArrayList<>(NO_BADGES));
    }

    @Test
    void whenCreate_thenCategoryIsSenior() {
        TravelerCategory actual = sut.getCategory();
        assertEquals(EXPECTED_CATEGORY, actual);
    }

    @Test
    void whenBookZeroG_thenExperienceIsBookedWithTravelerId() {
        sut.bookZeroGravityExperience(zeroGravity, EMPTY_BOOKING);
        verify(zeroGravity).book(ANY_ID);
    }

    @Test
    void givenAlreadyHasStillGotIt_whenBookZeroG_thenNoDuplicateBadge() {
        SeniorTraveler withBadge = new SeniorTraveler(ANY_ID, ANY_NAME, new ArrayList<>(WITH_STILL_GOT_IT));

        withBadge.bookZeroGravityExperience(zeroGravity, EMPTY_BOOKING);

        int count = (int) withBadge.getBadges().stream().filter(Badge.STILL_GOT_IT::equals).count();
        assertEquals(ONE, count);
    }
}
