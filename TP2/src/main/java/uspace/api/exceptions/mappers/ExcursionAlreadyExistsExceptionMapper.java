package uspace.api.exceptions.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import uspace.api.exceptions.ErrorResponse;
import uspace.domain.cruise.itinerary.planet.excursion.exceptions.ExcursionAlreadyExistsException;

@Provider
public class ExcursionAlreadyExistsExceptionMapper implements ExceptionMapper<ExcursionAlreadyExistsException> {
    @Override
    public Response toResponse(ExcursionAlreadyExistsException ex) {
        return Response.status(400)
                .entity(new ErrorResponse("EXCURSION_ALREADY_EXISTS",
                        "Excursion already exists in the cruise itinerary."))
                .build();
    }
}
