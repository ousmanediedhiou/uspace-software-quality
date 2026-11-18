package uspace.domain.cruise.zeroGravityExperience;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.domain.cruise.booking.traveler.TravelerId;
import uspace.domain.cruise.zeroGravityExperience.exceptions.ZeroGravityExperienceAlreadyBookedException;
import uspace.domain.cruise.zeroGravityExperience.exceptions.ZeroGravityExperienceFullException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ZeroGravityExperienceTest {

    private final ZeroGravityExperience experience = new ZeroGravityExperience(10);

    @Mock private TravelerId travelerA;
    @Mock private TravelerId travelerB;
    @Mock private TravelerId travelerC;
    @Mock private TravelerId travelerD;
    @Mock private TravelerId travelerE;
    @Mock private TravelerId travelerF;
    @Mock private TravelerId travelerG;
    @Mock private TravelerId travelerH;
    @Mock private TravelerId travelerI;
    @Mock private TravelerId travelerJ;
    @Mock private TravelerId travelerK;

    @Test
    void givenNotBooked_whenBook_thenSucceeds() {

        assertDoesNotThrow(() -> experience.book(travelerA));
    }

    @Test
    void givenAlreadyBooked_whenBook_thenThrowsAlreadyBooked() {

        experience.book(travelerA);

        assertThrows(ZeroGravityExperienceAlreadyBookedException.class,
                () -> experience.book(travelerA));
    }

    @Test
    void givenCapacityNotReached_whenBookTen_thenAllSucceed() {

        assertDoesNotThrow(() -> experience.book(travelerA));
        assertDoesNotThrow(() -> experience.book(travelerB));
        assertDoesNotThrow(() -> experience.book(travelerC));
        assertDoesNotThrow(() -> experience.book(travelerD));
        assertDoesNotThrow(() -> experience.book(travelerE));
        assertDoesNotThrow(() -> experience.book(travelerF));
        assertDoesNotThrow(() -> experience.book(travelerG));
        assertDoesNotThrow(() -> experience.book(travelerH));
        assertDoesNotThrow(() -> experience.book(travelerI));
        assertDoesNotThrow(() -> experience.book(travelerJ));
    }

    @Test
    void givenCapacityReached_whenBook_thenThrowsFull() {
        experience.book(travelerA);
        experience.book(travelerB);
        experience.book(travelerC);
        experience.book(travelerD);
        experience.book(travelerE);
        experience.book(travelerF);
        experience.book(travelerG);
        experience.book(travelerH);
        experience.book(travelerI);
        experience.book(travelerJ);

        assertThrows(ZeroGravityExperienceFullException.class,
                () -> experience.book(travelerK));
    }
}
