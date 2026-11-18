package uspace.api.exceptions.mappers.uspace;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import uspace.domain.cruise.sport.exceptions.SportNotBookableException;

@Provider
public class SportNotBookableExceptionMapper implements ExceptionMapper<SportNotBookableException> {

    @Override
    public Response toResponse(SportNotBookableException exception) {
        ErrorPayload payload = new ErrorPayload("SPORT_NOT_BOOKABLE", "Sport not bookable for traveler");
        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(payload)
                .build();
    }

    private static class ErrorPayload {
        public String error;
        public String description;

        public ErrorPayload(String error, String description) {
            this.error = error;
            this.description = description;
        }
    }
}
