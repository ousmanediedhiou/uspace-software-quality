package uspace.api.planet;

public record NewPlanetRequest(
        String name,
        String arrivalDateTime,   // ex: 2085-06-05T09:00
        String departureDateTime  //
) { }

