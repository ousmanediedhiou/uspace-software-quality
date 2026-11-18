package uspace.api.planet;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uspace.api.exceptions.ErrorResponse;
import uspace.application.cruise.exceptions.CruiseNotFoundException;
import uspace.application.planet.NewPlanetDto;
import uspace.application.planet.PlanetService;
import uspace.domain.cruise.itinerary.exceptions.InvalidPlanetDateException;
import uspace.domain.cruise.itinerary.exceptions.InvalidPlanetException;
import uspace.domain.cruise.itinerary.exceptions.PlanetAlreadyExistsException;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@Path("/cruises/{cruiseId}/planets")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PlanetResource {

    @Inject
    PlanetService planetService;

    @POST
    public Response addPlanet(@PathParam("cruiseId") String cruiseId, NewPlanetRequest body) {
        if (body == null || isBlank(body.name())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("MISSING_PARAMETER", "Missing parameter: name"))
                    .build();
        }
        if (isBlank(body.arrivalDateTime())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("MISSING_PARAMETER", "Missing parameter: arrivalDateTime"))
                    .build();
        }
        if (isBlank(body.departureDateTime())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("MISSING_PARAMETER", "Missing parameter: departureDateTime"))
                    .build();
        }

        final LocalDateTime arrival;
        final LocalDateTime departure;
        try {
            arrival = LocalDateTime.parse(body.arrivalDateTime());
            departure = LocalDateTime.parse(body.departureDateTime());
        } catch (DateTimeParseException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("INVALID_DATE_FORMAT", "Invalid date format."))
                    .build();
        }

        try {
            planetService.addPlanet(
                    cruiseId,
                    new NewPlanetDto(body.name(), arrival, departure)
            );
            return Response.status(Response.Status.CREATED).build();

        } catch (CruiseNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("CRUISE_NOT_FOUND", "Cruise not found"))
                    .build();

        } catch (PlanetAlreadyExistsException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("PLANET_ALREADY_EXISTS", "Planet already exists in the itinerary"))
                    .build();

        } catch (InvalidPlanetDateException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("INVALID_PLANET_DATE", "Arrival date or departure date is invalid"))
                    .build();

        } catch (InvalidPlanetException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("INVALID_PLANET", "Invalid planet"))
                    .build();
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
