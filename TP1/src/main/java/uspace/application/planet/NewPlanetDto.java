package uspace.application.planet;

import java.time.LocalDateTime;

public record NewPlanetDto(String name, LocalDateTime arrival, LocalDateTime departure) { }
