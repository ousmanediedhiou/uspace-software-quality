package uspace.domain.cruise.emergencyShuttles;

import uspace.infra.emergencyShuttles.dtos.ShuttleConfigurationDto;

public interface ShuttlesConfiguration {
    public ShuttleConfigurationDto getShuttleConfigurationByType(ShuttleType type);
}
