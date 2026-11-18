package uspace.application.cruise.emergencyShuttles.dtos;

public class ShuttleTravelerDto {
    public String travelerId;
    public String travelerName;

    public ShuttleTravelerDto() {}

    public ShuttleTravelerDto(String travelerId, String travelerName) {
        this.travelerId = travelerId;
        this.travelerName = travelerName;
    }
}
