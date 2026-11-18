package uspace.api.cruise.cabin;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uspace.application.cruise.cabin.CabinAttributionService;
import uspace.application.cruise.cabin.dtos.CabinAttributionsDto;

@Path("/cruises/{cruiseId}/cabins")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CabinResource {

    private final CabinAttributionService cabinAttributionService;

    @Inject
    public CabinResource(CabinAttributionService cabinAttributionService) {
        this.cabinAttributionService = cabinAttributionService;
    }

    @GET
    public Response getCabins(@PathParam("cruiseId") String cruiseId,
                              @QueryParam("criteria") String criteria) {
        CabinAttributionsDto dto = cabinAttributionService.getCabinAttributions(cruiseId, criteria);
        return Response.ok(dto).build();
    }
}
