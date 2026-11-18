package uspace.application.cruise.emergencyShuttles;

import jakarta.inject.Inject;
import uspace.application.cruise.emergencyShuttles.dtos.EmergencyShuttlesDto;
import uspace.application.cruise.emergencyShuttles.dtos.ShuttleDto;
import uspace.domain.cruise.emergencyShuttles.EmergencyShuttles;
import uspace.domain.cruise.emergencyShuttles.Shuttle;

import java.util.List;

public class EmergencyShuttlesAssembler {

    private final ShuttleAssembler shuttleAssembler;

    @Inject
    public EmergencyShuttlesAssembler(ShuttleAssembler shuttleAssembler){
        this.shuttleAssembler = shuttleAssembler;
    }

    public EmergencyShuttlesDto toDto(EmergencyShuttles emergencyShuttles){
        List<ShuttleDto> shuttleDtos = createShuttleDtos(emergencyShuttles.getShuttles());
        Integer cost = (int) emergencyShuttles.getCost().toFloat();

        return new EmergencyShuttlesDto(
                cost,
                shuttleDtos
        );
    }

    private List<ShuttleDto> createShuttleDtos(List<Shuttle> shuttles){
        return shuttles.stream().map(shuttleAssembler::toDto).toList();
    }
}
