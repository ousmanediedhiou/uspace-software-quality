package uspace.application.cruise.emergencyShuttles.dtos;

public class ShuttleCrewMemberDto {
    public String employeeId;
    public String crewMemberName;

    public ShuttleCrewMemberDto() {}

    public ShuttleCrewMemberDto(String employeeId, String crewMemberName) {
        this.employeeId = employeeId;
        this.crewMemberName = crewMemberName;
    }
}
