package uspace.domain.cruise.booking.traveler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class TravelerFactoryTest {

    @InjectMocks
    private TravelerFactory travelerFactory;

    @Test
    void whenCreateAdult_thenReturnsAdultTraveler() {

        Traveler t = travelerFactory.create("Bouba", TravelerCategory.ADULT);

        assertTrue(t instanceof AdultTraveler);
    }

    @Test
    void whenCreateSenior_thenReturnsSeniorTraveler() {
        Traveler t = travelerFactory.create("Seydou", TravelerCategory.SENIOR);
        assertTrue(t instanceof SeniorTraveler);
    }

    @Test
    void whenCreateChild_thenReturnsChildTraveler() {
        Traveler t = travelerFactory.create("KONA", TravelerCategory.CHILD);
        assertTrue(t instanceof ChildTraveler);
    }
}
