package uspace.application.cruise.crew;

import jakarta.inject.Singleton;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.cabin.CabinType;

@Singleton
public class StandardCrewCabinPolicy implements CabinAllocationPolicy {
    @Override
    public void allocateForCrew(Cruise cruise, CabinType ignored) {
        cruise.bookCabin(CabinType.STANDARD);
    }
}
