package uspace.api.exceptions.mappers.uspace;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import uspace.domain.cruise.sport.exceptions.SportAlreadyBookedException;

@Provider
public class SportAlreadyBookedExceptionMapper implements ExceptionMapper<SportAlreadyBookedException> {

    @Override
    public Response toResponse(SportAlreadyBookedException exception) {
        ErrorPayload payload = new ErrorPayload("SPORT_ALREADY_BOOKED", "Sport already booked by traveler");
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
