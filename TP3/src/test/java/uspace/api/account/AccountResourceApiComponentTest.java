package uspace.api.account;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Test;
import uspace.api.exceptions.mappers.account.AccountCannotBeDeletedExceptionMapper;
import uspace.api.exceptions.mappers.account.AccountNotFoundExceptionMapper;
import uspace.application.account.AccountService;
import uspace.application.account.dtos.AccountDto;
import uspace.domain.account.exceptions.AccountCannotBeDeletedException;
import uspace.domain.account.exceptions.AccountNotFoundException;
import uspace.domain.account.exceptions.UsernameAlreadyExistsException;
import uspace.domain.exceptions.MissingParameterException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AccountResourceApiComponentTest extends JerseyTest {

    private static final String ANY_USERNAME = "ANY_USERNAME";
    private static final String VALID_USERNAME = "VALID_USERNAME";
    private static final String VALID_HOME_PLANET = "VALID_HOME_PLANET";
    private static final String UNKNOWN_USERNAME = "UNKNOWN_USERNAME";
    private static final String PATH_ACCOUNTS = "/accounts";

    private AccountService accountServiceMock;
    private AccountDtoValidator accountDtoValidatorMock;

    @Override
    protected Application configure() {
        accountServiceMock = mock(AccountService.class);
        accountDtoValidatorMock = mock(AccountDtoValidator.class);

        return new ResourceConfig()
                .packages("uspace")
                .register(JacksonFeature.withoutExceptionMappers())
                .register(new AccountResource(accountServiceMock, accountDtoValidatorMock))
                .register(AccountNotFoundExceptionMapper.class)
                .register(AccountCannotBeDeletedExceptionMapper.class);
    }

    @Test
    void givenAccountExistsAndIsDeletable_whenDeleteAccount_thenReturnHttp200() {
        doNothing().when(accountServiceMock).deleteAccount(ANY_USERNAME);

        Response response = whenDeleteAccount();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(accountServiceMock).deleteAccount(ANY_USERNAME);
    }

    @Test
    void givenAccountDoesNotExist_whenDeleteAccount_thenReturnHttp404() {
        doThrow(new AccountNotFoundException()).when(accountServiceMock).deleteAccount(ANY_USERNAME);

        Response response = whenDeleteAccount();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(accountServiceMock).deleteAccount(ANY_USERNAME);
    }

    @Test
    void givenAccountCannotBeDeleted_whenDeleteAccount_thenReturnHttp400() {
        doThrow(new AccountCannotBeDeletedException()).when(accountServiceMock).deleteAccount(ANY_USERNAME);

        Response response = whenDeleteAccount();

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        verify(accountServiceMock).deleteAccount(ANY_USERNAME);
    }

    @Test
    void givenValidAccountDto_whenCreateAccount_thenReturnHttp201() {
        AccountDto dto = validAccountDto();

        Response response = target(PATH_ACCOUNTS)
                .request()
                .post(Entity.json(dto));

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        verify(accountDtoValidatorMock).validate(any(AccountDto.class));
        verify(accountServiceMock).createAccount(any(AccountDto.class));
    }

    @Test
    void givenMissingUsername_whenCreateAccount_thenReturnHttp400() {
        AccountDto dto = new AccountDto();
        dto.username = null;
        dto.homePlanetName = VALID_HOME_PLANET;

        doThrow(new MissingParameterException("username"))
                .when(accountDtoValidatorMock).validate(any(AccountDto.class));

        Response response = target(PATH_ACCOUNTS)
                .request()
                .post(Entity.json(dto));

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        verify(accountDtoValidatorMock).validate(any(AccountDto.class));
        verify(accountServiceMock, never()).createAccount(any(AccountDto.class));
    }

    @Test
    void givenMissingHomePlanetName_whenCreateAccount_thenReturnHttp400() {
        AccountDto dto = new AccountDto();
        dto.username = VALID_USERNAME;
        dto.homePlanetName = null;

        doThrow(new MissingParameterException("homePlanetName"))
                .when(accountDtoValidatorMock).validate(any(AccountDto.class));

        Response response = target(PATH_ACCOUNTS)
                .request()
                .post(Entity.json(dto));

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        verify(accountDtoValidatorMock).validate(any(AccountDto.class));
        verify(accountServiceMock, never()).createAccount(any(AccountDto.class));
    }

    @Test
    void givenUsernameAlreadyExists_whenCreateAccount_thenReturnHttp400() {
        AccountDto dto = validAccountDto();

        doNothing().when(accountDtoValidatorMock).validate(any(AccountDto.class));
        doThrow(new UsernameAlreadyExistsException())
                .when(accountServiceMock).createAccount(any(AccountDto.class));

        Response response = target(PATH_ACCOUNTS)
                .request()
                .post(Entity.json(dto));

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        verify(accountDtoValidatorMock).validate(any(AccountDto.class));
        verify(accountServiceMock).createAccount(any(AccountDto.class));
    }

    @Test
    void givenAccountExists_whenGetAccount_thenReturnHttp200() {
        AccountDto dto = validAccountDto();
        when(accountServiceMock.findAccount(VALID_USERNAME)).thenReturn(dto);

        Response response = target(PATH_ACCOUNTS + "/" + VALID_USERNAME)
                .request()
                .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(accountServiceMock).findAccount(VALID_USERNAME);
    }

    @Test
    void givenAccountDoesNotExist_whenGetAccount_thenReturnHttp404() {
        doThrow(new AccountNotFoundException())
                .when(accountServiceMock).findAccount(UNKNOWN_USERNAME);

        Response response = target(PATH_ACCOUNTS + "/" + UNKNOWN_USERNAME)
                .request()
                .get();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(accountServiceMock).findAccount(UNKNOWN_USERNAME);
    }

    private Response whenDeleteAccount() {
        Invocation.Builder request = target(PATH_ACCOUNTS + "/" + ANY_USERNAME).request();
        return request.delete();
    }

    private AccountDto validAccountDto() {
        AccountDto dto = new AccountDto();
        dto.username = VALID_USERNAME;
        dto.homePlanetName = VALID_HOME_PLANET;
        return dto;
    }
}
