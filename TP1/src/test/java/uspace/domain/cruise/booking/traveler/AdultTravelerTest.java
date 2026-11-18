package uspace.domain.cruise.booking.traveler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.domain.cruise.booking.traveler.badge.Badge;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdultTravelerTest {

    @Mock private TravelerId travelerId;
    @Mock private TravelerName travelerName;
    @Mock private List<Badge> badges;
    @Mock private ZeroGravityExperience zeroGravityExperience;

    @InjectMocks
    private AdultTraveler adultTraveler;

    @Test
    void whenBookZeroG_thenDelegatesToExperience() {
        adultTraveler.bookZeroGravityExperience(zeroGravityExperience);

        verify(zeroGravityExperience, times(1)).book(travelerId);
    }

    @Test
    void whenBookZeroG_thenAddsZeroGBadge() {
        adultTraveler.bookZeroGravityExperience(zeroGravityExperience);

        verify(badges, times(1)).add(Badge.ZERO_G);
    }
}
