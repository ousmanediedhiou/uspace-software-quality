package uspace.api.exceptions.mappers;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import uspace.application.account.AccountNotFoundException;

import java.util.Map;

@Provider
public class AccountNotFoundExceptionMapper implements ExceptionMapper<AccountNotFoundException> {

    @Override
    public Response toResponse(AccountNotFoundException e) {
        return Response.status(Response.Status.NOT_FOUND)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of(
                        "error", "ACCOUNT_NOT_FOUND",
                        "description", "Account not found."
                ))
                .build();
    }
}
