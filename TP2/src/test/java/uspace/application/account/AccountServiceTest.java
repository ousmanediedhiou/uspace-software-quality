package uspace.application.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.application.account.dtos.AccountDto;
import uspace.application.account.dtos.NewAccountDto;
import uspace.domain.account.Account;
import uspace.domain.account.AccountRepository;
import uspace.domain.exceptions.MissingParameterException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    private static final String USERNAME = "john";
    private static final String HOME_PLANET = "Earth";
    private static final String BOOKING_ID = "BID-001";

    @Mock private AccountRepository accountRepository;
    @Mock private AccountAssembler accountAssembler;

    private AccountService service;

    @BeforeEach
    void setup() {
        service = new AccountService();
        service.accountRepository = accountRepository;
        service.accountAssembler = accountAssembler;
    }

    @Test
    void createAccount_whenValidPayload_thenSave() {
        NewAccountDto dto = new NewAccountDto();
        dto.username = USERNAME;
        dto.homePlanetName = HOME_PLANET;

        Account account = mock(Account.class);
        when(accountRepository.existsByUsername(USERNAME)).thenReturn(false);
        when(accountAssembler.toDomain(dto)).thenReturn(account);

        service.createAccount(dto);

        verify(accountRepository).save(account);
    }

    @Test
    void createAccount_whenUsernameMissing_thenThrowMissingParameterException() {
        NewAccountDto dto = new NewAccountDto();
        dto.username = null;
        dto.homePlanetName = HOME_PLANET;

        Executable call = () -> service.createAccount(dto);

        assertThrows(MissingParameterException.class, call);
    }

    @Test
    void createAccount_whenHomePlanetMissing_thenThrowMissingParameterException() {
        NewAccountDto dto = new NewAccountDto();
        dto.username = USERNAME;
        dto.homePlanetName = null;

        Executable call = () -> service.createAccount(dto);

        assertThrows(MissingParameterException.class, call);
    }

    @Test
    void createAccount_whenUsernameExists_thenThrowUsernameAlreadyExistsException() {
        NewAccountDto dto = new NewAccountDto();
        dto.username = USERNAME;
        dto.homePlanetName = HOME_PLANET;

        when(accountRepository.existsByUsername(USERNAME)).thenReturn(true);

        Executable call = () -> service.createAccount(dto);

        assertThrows(UsernameAlreadyExistsException.class, call);
    }

    @Test
    void findByUsername_whenFound_thenReturnsDto() {
        Account domain = mock(Account.class);
        AccountDto dto = new AccountDto(USERNAME, HOME_PLANET, java.util.List.of());
        when(accountRepository.findByUsername(USERNAME)).thenReturn(domain);
        when(accountAssembler.toDto(domain)).thenReturn(dto);

        AccountDto result = service.findByUsername(USERNAME);

        assertSame(dto, result);
    }

    @Test
    void findByUsername_whenUsernameMissing_thenThrowMissingParameterException() {
        Executable call = () -> service.findByUsername("  ");

        assertThrows(MissingParameterException.class, call);
    }

    @Test
    void findByUsername_whenNotFound_thenThrowAccountNotFoundException() {
        when(accountRepository.findByUsername(USERNAME)).thenReturn(null);

        Executable call = () -> service.findByUsername(USERNAME);

        assertThrows(AccountNotFoundException.class, call);
    }

    @Test
    void addBookingToAccount_whenFound_thenSaveWithNewBookingId() {
        Account domain = mock(Account.class);
        when(accountRepository.findByUsername(USERNAME)).thenReturn(domain);

        service.addBookingToAccount(USERNAME, BOOKING_ID);

        verify(accountRepository).save(domain);
    }

    @Test
    void addBookingToAccount_whenNotFound_thenThrowAccountNotFoundException() {
        when(accountRepository.findByUsername(USERNAME)).thenReturn(null);

        Executable call = () -> service.addBookingToAccount(USERNAME, BOOKING_ID);

        assertThrows(AccountNotFoundException.class, call);
    }
}
