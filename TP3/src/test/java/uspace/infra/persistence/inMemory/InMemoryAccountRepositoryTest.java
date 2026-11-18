package uspace.infra.persistence.inMemory;

import org.junit.jupiter.api.Test;
import uspace.domain.account.Account;
import uspace.domain.account.AccountUsername;

import static org.junit.jupiter.api.Assertions.assertSame;

class InMemoryAccountRepositoryTest {

    private static final AccountUsername ANY_USERNAME = new AccountUsername("UNIT_TEST_USERNAME");

    @Test
    void givenSavedAccount_whenFindByUsername_thenReturnSameAccount() {
        InMemoryAccountRepository repository = new InMemoryAccountRepository();
        Account account = new Account(ANY_USERNAME, null);

        repository.save(account);
        Account found = repository.findByUsername(ANY_USERNAME);

        assertSame(account, found);
    }
}
