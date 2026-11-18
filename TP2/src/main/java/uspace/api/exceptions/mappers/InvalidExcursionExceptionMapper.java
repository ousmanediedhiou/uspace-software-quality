package uspace.api.exceptions.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import uspace.api.exceptions.ErrorResponse;
import uspace.domain.cruise.itinerary.planet.excursion.exceptions.InvalidExcursionException;

@Provider
public class InvalidExcursionExceptionMapper implements ExceptionMapper<InvalidExcursionException> {
    @Override
    public Response toResponse(InvalidExcursionException ex) {
        return Response.status(400)
                .entity(new ErrorResponse("INVALID_EXCURSION", "Invalid excursion."))
                .build();
    }
}
