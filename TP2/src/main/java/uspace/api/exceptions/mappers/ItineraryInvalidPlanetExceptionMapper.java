package uspace.api.exceptions.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import uspace.api.exceptions.ErrorResponse;
import uspace.domain.cruise.itinerary.exceptions.InvalidPlanetException;

@Provider
public class ItineraryInvalidPlanetExceptionMapper implements ExceptionMapper<InvalidPlanetException> {
    @Override
    public Response toResponse(InvalidPlanetException ex) {
        return Response.status(404)
                .entity(new ErrorResponse("PLANET_NOT_FOUND", "Planet not found."))
                .build();
    }
}
