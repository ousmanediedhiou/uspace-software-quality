package uspace.application.cruise.crew;

import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.cabin.CabinType;

public interface CabinAllocationPolicy {
    void allocateForCrew(Cruise cruise, CabinType cabinType);
}
