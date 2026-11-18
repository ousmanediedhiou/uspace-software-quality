package uspace.api.cruise.planet;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uspace.application.cruise.planet.PlanetService;
import uspace.application.cruise.planet.dtos.PlanetDto;

@Path("/cruises/{cruiseId}/planets")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PlanetResource {

    private final PlanetService planetService;
    private final PlanetDtoValidator planetDtoValidator;

    @Inject
    public PlanetResource(PlanetService planetService, PlanetDtoValidator planetDtoValidator) {
        this.planetService = planetService;
        this.planetDtoValidator = planetDtoValidator;
    }

    @POST
    public Response addPlanet(@PathParam("cruiseId") String cruiseId, PlanetDto planetDto) {
        planetDtoValidator.validate(planetDto);
        planetService.addPlanetToItinerary(cruiseId, planetDto);

        return Response.status(Response.Status.CREATED).build();
    }
}
