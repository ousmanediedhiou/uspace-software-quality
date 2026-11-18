package uspace.api.cruise.planet.excursion;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uspace.application.cruise.planet.excursion.ExcursionService;
import uspace.application.cruise.planet.excursion.dtos.NewExcursionDto;

@Path("/cruises/{cruiseId}/planets/{planetName}/excursions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ExcursionResource {

    private final ExcursionService service;

    @Inject
    public ExcursionResource(ExcursionService service) {
        this.service = service;
    }

    @POST
    public Response add(@PathParam("cruiseId") String cruiseId,
                        @PathParam("planetName") String planetName,
                        NewExcursionDto dto) {
        service.addExcursionToPlanet(cruiseId, planetName, dto);
        return Response.status(201).build();
    }
}
