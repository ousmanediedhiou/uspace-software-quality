package uspace.application.cruise.emergencyShuttles;

import uspace.application.cruise.emergencyShuttles.dtos.ShuttleTravelerDto;
import uspace.domain.cruise.emergencyShuttles.ShuttleTraveler;

public class ShuttleTravelerAssembler {
    public ShuttleTravelerDto toDto(ShuttleTraveler traveler){
        return new ShuttleTravelerDto(
                traveler.getId().toString(),
                traveler.getName().toString()
        );
    }
}
