package uspace.application.cruise.emergencyShuttles;

import uspace.application.cruise.emergencyShuttles.dtos.ShuttleCrewMemberDto;
import uspace.domain.cruise.emergencyShuttles.ShuttleCrewMember;

public class ShuttleCrewMemberAssembler {
    public ShuttleCrewMemberDto toDto(ShuttleCrewMember shuttleCrewMember) {
        return new ShuttleCrewMemberDto(
                shuttleCrewMember.getEmployeeId().toString(),
                shuttleCrewMember.getName().toString()
        );
    }
}
