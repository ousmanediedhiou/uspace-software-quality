package uspace.api.exceptions.mappers;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import uspace.application.account.UsernameAlreadyExistsException;

import java.util.Map;

@Provider
public class UsernameAlreadyExistsExceptionMapper implements ExceptionMapper<UsernameAlreadyExistsException> {

    @Override
    public Response toResponse(UsernameAlreadyExistsException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of(
                        "error", "USERNAME_ALREADY_EXISTS",
                        "description", "Username already exists."
                ))
                .build();
    }
}
