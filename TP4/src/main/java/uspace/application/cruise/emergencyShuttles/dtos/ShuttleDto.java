package uspace.application.cruise.emergencyShuttles.dtos;

import java.util.List;

public class ShuttleDto {
    public String type;
    public List<ShuttleTravelerDto> travelers;
    public List<ShuttleCrewMemberDto> crewMembers;

    public ShuttleDto(){}

    public ShuttleDto(String type, List<ShuttleTravelerDto> travelers, List<ShuttleCrewMemberDto> crewMembers) {
        this.type = type;
        this.travelers = travelers;
        this.crewMembers = crewMembers;
    }
}
