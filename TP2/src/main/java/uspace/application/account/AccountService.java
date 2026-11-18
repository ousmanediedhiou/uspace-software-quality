package uspace.application.account;

import jakarta.inject.Inject;
import uspace.application.account.dtos.AccountDto;
import uspace.application.account.dtos.NewAccountDto;
import uspace.domain.account.Account;
import uspace.domain.account.AccountRepository;
import uspace.domain.exceptions.MissingParameterException;

public class AccountService {

    @Inject
    AccountRepository accountRepository;

    @Inject
    AccountAssembler accountAssembler;

    public void createAccount(NewAccountDto dto) {
        if (dto == null || dto.username == null || dto.username.isBlank()) {
            throw new MissingParameterException("username");
        }
        if (dto.homePlanetName == null || dto.homePlanetName.isBlank()) {
            throw new MissingParameterException("homePlanetName");
        }
        if (accountRepository.existsByUsername(dto.username)) {
            throw new UsernameAlreadyExistsException();
        }
        Account account = accountAssembler.toDomain(dto);
        accountRepository.save(account);
    }

    public AccountDto findByUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new MissingParameterException("username");
        }
        Account found = accountRepository.findByUsername(username);
        if (found == null) {
            throw new AccountNotFoundException();
        }
        return accountAssembler.toDto(found);
    }

    public void addBookingToAccount(String username, String bookingId) {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new AccountNotFoundException();
        }
        account.addBookingId(bookingId);
        accountRepository.save(account);
    }
}
