package uspace.api.cruise;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uspace.api.cruise.booking.NewBookingDtoValidator;
import uspace.api.cruise.dtos.ZeroGravityExperienceBookingRequest;
import uspace.api.cruise.validators.ZeroGravityExperienceBookingRequestValidator;
import uspace.application.cruise.CruiseService;
import uspace.application.cruise.ZeroGBookingService;
import uspace.application.cruise.booking.BookingService;
import uspace.application.cruise.booking.dtos.newBooking.NewBookingDto;
import uspace.application.cruise.dtos.CruiseDto;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.booking.BookingId;
import uspace.domain.cruise.booking.traveler.TravelerId;
import uspace.domain.cruise.dateTime.CruiseDateTime;

import java.time.LocalDateTime;

@Path("/cruises")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CruiseResource {

    private final CruiseService cruiseService;
    private final ZeroGBookingService zeroGBookingService;
    private final BookingService bookingService;

    @Inject
    public CruiseResource(CruiseService cruiseService, ZeroGBookingService zeroGBookingService, BookingService bookingService) {
        this.cruiseService = cruiseService;
        this.zeroGBookingService = zeroGBookingService;
        this.bookingService = bookingService;
    }

    @GET
    @Path("{cruiseId}")
    public Response getCruise(@PathParam("cruiseId") String cruiseId) {
        CruiseDto cruiseDto = cruiseService.findCruise(cruiseId);
        return Response.ok(cruiseDto).build();
    }

    @POST
    @Path("/{cruiseId}/bookings/{bookingId}/travelers/{travelerId}/zeroGravityExperiences")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response bookZeroGravityExperience(@PathParam("cruiseId") String cruiseId,
                                              @PathParam("bookingId") String bookingId,
                                              @PathParam("travelerId") String travelerId,
                                              ZeroGravityExperienceBookingRequest request) {

        LocalDateTime bookingDateTime = ZeroGravityExperienceBookingRequestValidator.validateAndParse(request);

        zeroGBookingService.bookZeroG(
                new CruiseId(cruiseId),
                new BookingId(bookingId),
                new TravelerId(travelerId),
                new CruiseDateTime(bookingDateTime)
        );

        return Response.ok().build();

    }

    @POST
    @Path("/{cruiseId}/bookings")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBooking(@PathParam("cruiseId") String cruiseId,
                                  NewBookingDto request) {

        new NewBookingDtoValidator().validate(request);

        var result = bookingService.createBooking(cruiseId, request);

        return Response.status(Response.Status.CREATED).entity(result).build();
    }
}
