package uspace.api.errors;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import uspace.api.exceptions.ErrorResponse;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable ex) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(new ErrorResponse("INTERNAL_ERROR",
                        ex.getClass().getSimpleName() + ": " + String.valueOf(ex.getMessage())))
                .build();
    }
}
