package uspace.infra.persistence.inMemory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uspace.domain.account.Account;
import uspace.domain.account.AccountRepository;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryAccountRepositoryTest {

    private static final String USERNAME = "john";
    private static final String HOME_PLANET = "Earth";
    private static final String BOOKING_ID = "BID-001";

    private AccountRepository repo;

    @BeforeEach
    void setup() {
        repo = new InMemoryAccountRepository();
    }

    @Test
    void existsByUsername_whenNotSaved_thenFalse() {
        boolean result = repo.existsByUsername(USERNAME);

        assertFalse(result);
    }

    @Test
    void existsByUsername_whenSaved_thenTrue() {
        repo.save(new Account(USERNAME, HOME_PLANET));

        boolean result = repo.existsByUsername(USERNAME);

        assertTrue(result);
    }

    @Test
    void findByUsername_whenSaved_thenReturnAccount() {
        Account acc = new Account(USERNAME, HOME_PLANET);
        repo.save(acc);

        Account found = repo.findByUsername(USERNAME);

        assertEquals(USERNAME, found.getUsername());
    }

    @Test
    void save_whenAddBookingId_thenStateIsPersisted() {
        Account acc = new Account(USERNAME, HOME_PLANET);
        acc.addBookingId(BOOKING_ID);
        repo.save(acc);

        Account found = repo.findByUsername(USERNAME);

        assertTrue(found.getBookingIds().contains(BOOKING_ID));
    }
}
