package uspace.api.cruise;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import uspace.api.cruise.dtos.ZeroGravityExperienceBookingRequest;
import uspace.api.cruise.validators.ZeroGravityExperienceBookingRequestValidator;
import uspace.application.cruise.ZeroGBookingService;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.booking.BookingId;
import uspace.domain.cruise.booking.traveler.TravelerId;
import uspace.domain.cruise.dateTime.CruiseDateTime;

import java.time.LocalDateTime;

@Path("/cruises/{cruiseId}/bookings/{bookingId}/travelers/{travelerId}/zeroGravityExperiences")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ZeroGResource {

    private final ZeroGBookingService zeroGBookingService;

    @Inject
    public ZeroGResource(ZeroGBookingService zeroGBookingService) {
        this.zeroGBookingService = zeroGBookingService;
    }

    @POST
    public Response bookZeroG(@PathParam("cruiseId") String cruiseId,
                              @PathParam("bookingId") String bookingId,
                              @PathParam("travelerId") String travelerId,
                              ZeroGravityExperienceBookingRequest request) {

        LocalDateTime dt = ZeroGravityExperienceBookingRequestValidator.validateAndParse(request);

        zeroGBookingService.bookZeroG(
                new CruiseId(cruiseId),
                new BookingId(bookingId),
                new TravelerId(travelerId),
                new CruiseDateTime(dt)
        );

        return Response.ok().build();
    }

    @GET @Path("/ping")
    public Response ping() { return Response.ok("OK").build(); }
}
