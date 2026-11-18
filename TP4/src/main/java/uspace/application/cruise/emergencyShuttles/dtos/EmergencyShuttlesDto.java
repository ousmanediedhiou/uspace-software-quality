package uspace.application.cruise.emergencyShuttles.dtos;

import java.util.List;

public class EmergencyShuttlesDto {
    public Integer cost;
    public List<ShuttleDto> emergencyShuttles;

    public EmergencyShuttlesDto(){}

    public EmergencyShuttlesDto(Integer cost, List<ShuttleDto> emergencyShuttles){
        this.cost = cost;
        this.emergencyShuttles = emergencyShuttles;
    }

    public Integer getCost() {
        return cost;
    }

    public List<ShuttleDto> getEmergencyShuttles(){
        return emergencyShuttles;
    }
}
