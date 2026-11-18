package uspace.infra.emergencyShuttles.dtos;

import java.util.List;

public class ShuttlesConfigurationFileDto {
    public List<ShuttleConfigurationDto> shuttleTypes;

    public ShuttlesConfigurationFileDto() {}

    public ShuttlesConfigurationFileDto(List<ShuttleConfigurationDto> shuttleTypes) {
        this.shuttleTypes = shuttleTypes;
    }
}
