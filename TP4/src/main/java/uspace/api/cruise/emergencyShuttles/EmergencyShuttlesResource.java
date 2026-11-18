package uspace.api.cruise.emergencyShuttles;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uspace.application.cruise.emergencyShuttles.EmergencyShuttlesService;
import uspace.application.cruise.emergencyShuttles.dtos.EmergencyShuttlesDto;

@Path("/cruises/{cruiseId}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EmergencyShuttlesResource {

    private final EmergencyShuttlesService emergencyShuttlesService;

    @Inject
    public EmergencyShuttlesResource(EmergencyShuttlesService emergencyShuttlesService) {
        this.emergencyShuttlesService = emergencyShuttlesService;
    }

    @GET
    @Path("emergencyShuttles")
    public Response getEmergencyShuttles(@PathParam("cruiseId") String cruiseId){
        EmergencyShuttlesDto emergencyShuttlesDto = emergencyShuttlesService.getEmergencyShuttlesManifest(cruiseId);
        return Response.ok(emergencyShuttlesDto).build();
    }
}
