package uspace.application.account;

import uspace.application.account.dtos.AccountDto;
import uspace.application.account.dtos.NewAccountDto;
import uspace.domain.account.Account;

public class AccountAssembler {

    public Account toDomain(NewAccountDto dto) {
        return new Account(dto.username, dto.homePlanetName);
    }

    public AccountDto toDto(Account account) {
        return new AccountDto(
                account.getUsername(),
                account.getHomePlanetName(),
                account.getBookingIds()
        );
    }
}
