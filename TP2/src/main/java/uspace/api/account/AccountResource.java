package uspace.api.account;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uspace.application.account.AccountService;
import uspace.application.account.dtos.AccountDto;
import uspace.application.account.dtos.NewAccountDto;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountResource {

    @Inject
    AccountService accountService;

    @Inject
    NewAccountDtoValidator validator;

    @POST
    public Response createAccount(NewAccountDto dto) {
        validator.validate(dto);
        accountService.createAccount(dto);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/{username}")
    public AccountDto getByUsername(@PathParam("username") String username) {
        return accountService.findByUsername(username);
    }
}
