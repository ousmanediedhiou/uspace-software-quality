package uspace.domain.cruise.booking.traveler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.domain.cruise.booking.traveler.badge.Badge;
import uspace.domain.cruise.booking.traveler.exceptions.ZeroGravityExperienceChildCriteriaException;
import uspace.domain.cruise.zeroGravityExperience.ZeroGravityExperience;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChildTravelerTest {

    @Mock private TravelerId childId;
    @Mock private TravelerName childName;
    @Mock private ZeroGravityExperience zeroGravityExperience;
    @Mock private List<Traveler> travelersInBooking;
    @Mock private Traveler adultTravelerMock;
    @Mock private List<Badge> adultBadges;

    private ChildTraveler childTraveler;
    private List<Badge> realBadges; // donnée inerte, pas un mock

    @BeforeEach
    void setUp() {
        realBadges = new ArrayList<>();
        childTraveler = new ChildTraveler(childId, childName, realBadges);
    }

    @Test
    void givenNoAdultOrSeniorAlreadyBooked_whenBookZeroG_thenThrows() {
        when(adultTravelerMock.getCategory()).thenReturn(TravelerCategory.ADULT);
        when(adultTravelerMock.getBadges()).thenReturn(adultBadges);
        when(adultBadges.contains(any())).thenReturn(false);
        when(travelersInBooking.stream()).thenReturn(Stream.of(adultTravelerMock));

        assertThrows(ZeroGravityExperienceChildCriteriaException.class,
                () -> childTraveler.bookZeroGravityExperience(zeroGravityExperience, travelersInBooking));

        verify(zeroGravityExperience, never()).book(any());
        assertTrue(realBadges.isEmpty());
    }

    @Test
    void givenAdultAlreadyBooked_whenBookZeroG_thenAddsMiniZeroG() {
        when(adultTravelerMock.getCategory()).thenReturn(TravelerCategory.ADULT);
        when(adultTravelerMock.getBadges()).thenReturn(adultBadges);
        when(adultBadges.contains(Badge.ZERO_G)).thenReturn(true);
        when(travelersInBooking.stream()).thenReturn(Stream.of(adultTravelerMock));

        childTraveler.bookZeroGravityExperience(zeroGravityExperience, travelersInBooking);

        assertTrue(realBadges.contains(Badge.MINI_ZERO_G));
        verify(zeroGravityExperience, times(1)).book(childId);
    }
}
