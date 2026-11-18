package uspace.api.exceptions.mappers.uspace;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import uspace.domain.cruise.sport.exceptions.SportNotFoundException;

@Provider
public class SportNotFoundExceptionMapper implements ExceptionMapper<SportNotFoundException> {

    @Override
    public Response toResponse(SportNotFoundException exception) {
        ErrorPayload payload = new ErrorPayload("SPORT_NOT_FOUND", "Sport not found");
        return Response.status(Response.Status.NOT_FOUND)
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
