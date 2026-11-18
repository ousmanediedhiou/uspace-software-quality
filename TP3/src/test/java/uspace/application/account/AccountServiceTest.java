package uspace.application.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uspace.application.account.dtos.AccountDto;
import uspace.domain.account.Account;
import uspace.domain.account.AccountRepository;
import uspace.domain.account.AccountUsername;
import uspace.domain.account.HomePlanetName;
import uspace.domain.account.exceptions.AccountCannotBeDeletedException;
import uspace.domain.account.exceptions.AccountNotFoundException;
import uspace.domain.account.exceptions.UsernameAlreadyExistsException;
import uspace.domain.cruise.booking.BookingId;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    private AccountService accountService;

    private static final String ANY_USERNAME = "ANY_USERNAME";

    @Mock
    private AccountRepository accountRepositoryMock;

    @Mock
    private AccountAssembler accountAssemblerMock;

    @Mock
    private Account accountMock;

    @Mock
    private HomePlanetName homePlanetNameMock;

    @Mock
    private BookingId bookingIdMock;

    @BeforeEach
    void setUp() {
        accountService = new AccountService(accountRepositoryMock, accountAssemblerMock);
    }

    @Test
    void givenAccountDoesNotExist_whenDeleteAccount_thenThrowAccountNotFoundException() {
        givenAccountDoesNotExist();

        Executable deleteAccount = () -> accountService.deleteAccount(ANY_USERNAME);

        assertThrows(AccountNotFoundException.class, deleteAccount);

        verify(accountRepositoryMock, never()).delete(any(Account.class));
    }

    @Test
    void givenAccountHasBookings_whenDeleteAccount_thenThrowAccountCannotBeDeletedException() {
        givenAccountExists();
        givenAccountHasBookings();

        Executable deleteAccount = () -> accountService.deleteAccount(ANY_USERNAME);

        assertThrows(AccountCannotBeDeletedException.class, deleteAccount);

        verify(accountRepositoryMock, never()).delete(any(Account.class));
    }

    @Test
    void givenAccountHasHomePlanetEarthIgnoringCase_whenDeleteAccount_thenThrowAccountCannotBeDeletedException() {
        givenAccountExists();
        givenAccountHasNoBookings();
        givenHomePlanetIsEarthIgnoringCase();

        Executable deleteAccount = () -> accountService.deleteAccount(ANY_USERNAME);

        assertThrows(AccountCannotBeDeletedException.class, deleteAccount);

        verify(accountRepositoryMock, never()).delete(any(Account.class));
    }

    @Test
    void givenAccountExistsWithoutBookingsAndHomePlanetNotEarth_whenDeleteAccount_thenDeleteAccount() {
        givenAccountExists();
        givenAccountHasNoBookings();
        givenHomePlanetIsNotEarth();

        accountService.deleteAccount(ANY_USERNAME);

        verify(accountRepositoryMock).delete(accountMock);
    }

    @Test
    void givenUsernameAlreadyExists_whenCreateAccount_thenThrowUsernameAlreadyExistsException() {
        givenAccountExists();
        AccountDto dto = anyAccountDto();

        Executable createAccount = () -> accountService.createAccount(dto);

        assertThrows(UsernameAlreadyExistsException.class, createAccount);

        verify(accountRepositoryMock, never()).save(any(Account.class));
    }

    @Test
    void givenUsernameAvailable_whenCreateAccount_thenSaveNewAccount() {
        givenAccountDoesNotExist();
        AccountDto dto = anyAccountDto();
        givenAssemblerCreatesAccount(dto, accountMock);

        accountService.createAccount(dto);

        verify(accountRepositoryMock).save(accountMock);
    }

    @Test
    void givenExistingAccount_whenFindAccount_thenReturnAssemblerDto() {
        givenAccountExists();
        AccountDto expectedDto = anyAccountDto();
        when(accountAssemblerMock.toDto(accountMock)).thenReturn(expectedDto);

        AccountDto result = accountService.findAccount(ANY_USERNAME);

        assertSame(expectedDto, result);
    }

    @Test
    void givenNonExistingAccount_whenFindAccount_thenThrowAccountNotFoundException() {
        givenAccountDoesNotExist();

        Executable find = () -> accountService.findAccount(ANY_USERNAME);

        assertThrows(AccountNotFoundException.class, find);
    }

    private void givenAccountDoesNotExist() {
        when(accountRepositoryMock.findByUsername(new AccountUsername(ANY_USERNAME))).thenReturn(null);
    }

    private void givenAccountExists() {
        when(accountRepositoryMock.findByUsername(new AccountUsername(ANY_USERNAME))).thenReturn(accountMock);
    }

    private void givenAccountHasBookings() {
        when(accountMock.getBookingIds()).thenReturn(List.of(bookingIdMock));
    }

    private void givenAccountHasNoBookings() {
        when(accountMock.getBookingIds()).thenReturn(Collections.emptyList());
    }

    private void givenHomePlanetIsEarthIgnoringCase() {
        when(accountMock.getHomePlanetName()).thenReturn(homePlanetNameMock);
        when(homePlanetNameMock.toString()).thenReturn("EaRtH");
    }

    private void givenHomePlanetIsNotEarth() {
        when(accountMock.getHomePlanetName()).thenReturn(homePlanetNameMock);
        when(homePlanetNameMock.toString()).thenReturn("Mars");
    }

    private AccountDto anyAccountDto() {
        AccountDto dto = new AccountDto();
        dto.username = ANY_USERNAME;
        dto.homePlanetName = "ANY_PLANET";
        return dto;
    }

    private void givenAssemblerCreatesAccount(AccountDto dto, Account account) {
        when(accountAssemblerMock.toAccount(dto)).thenReturn(account);
    }
}
