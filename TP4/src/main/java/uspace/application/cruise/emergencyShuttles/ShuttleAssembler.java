package uspace.application.cruise.emergencyShuttles;

import jakarta.inject.Inject;
import uspace.application.cruise.emergencyShuttles.dtos.ShuttleCrewMemberDto;
import uspace.application.cruise.emergencyShuttles.dtos.ShuttleTravelerDto;
import uspace.application.cruise.emergencyShuttles.dtos.ShuttleDto;
import uspace.domain.cruise.emergencyShuttles.Shuttle;

import java.util.List;

public class ShuttleAssembler {
    private final ShuttleTravelerAssembler shuttleTravelerAssembler;
    private final ShuttleCrewMemberAssembler shuttleCrewMemberAssembler;

    @Inject
    public  ShuttleAssembler(ShuttleTravelerAssembler shuttleTravelerAssembler, ShuttleCrewMemberAssembler shuttleCrewMemberAssembler) {
        this.shuttleTravelerAssembler = shuttleTravelerAssembler;
        this.shuttleCrewMemberAssembler = shuttleCrewMemberAssembler;
    }

    public ShuttleDto toDto(Shuttle shuttle){
        List<ShuttleTravelerDto> shuttleTravelerDtos = shuttle.getTravelers().stream().map(shuttleTravelerAssembler::toDto).toList();
        List<ShuttleCrewMemberDto> crewMemberDtos = shuttle.getCrewMembers().stream().map(shuttleCrewMemberAssembler::toDto).toList();

        return new ShuttleDto(
                shuttle.getType().toString(),
                shuttleTravelerDtos,
                crewMemberDtos
        );
    }
}
