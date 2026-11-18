package uspace.infra.persistence.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uspace.domain.account.Account;
import uspace.domain.account.AccountUsername;
import uspace.domain.account.HomePlanetName;

import static org.junit.jupiter.api.Assertions.*;

class HibernateAccountRepositoryComponentTest {

    private static final String ANY_USERNAME = "john_doe";
    private static final String ANY_HOME_PLANET_NAME = "Mars";
    private static final String UNKNOWN_USERNAME = "unknown_user";
    private static final String NEW_HOME_PLANET_NAME = "Jupiter";

    private HibernateAccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        accountRepository = new HibernateAccountRepository();
        clearAccounts();
    }

    @Test
    void givenNoAccountInDatabase_whenFindByUsername_thenReturnNull() {
        AccountUsername username = new AccountUsername(UNKNOWN_USERNAME);

        Account result = accountRepository.findByUsername(username);

        assertNull(result);
    }

    @Test
    void givenSavedAccount_whenFindByUsername_thenReturnPersistedAccount() {
        AccountUsername username = new AccountUsername(ANY_USERNAME);
        HomePlanetName homePlanetName = new HomePlanetName(ANY_HOME_PLANET_NAME);
        Account account = new Account(username, homePlanetName);

        accountRepository.save(account);

        Account persistedAccount = accountRepository.findByUsername(username);

        assertNotNull(persistedAccount);
        assertEquals(username, persistedAccount.getUsername());
        assertEquals(homePlanetName, persistedAccount.getHomePlanetName());
        assertNotNull(persistedAccount.getBookingIds());
    }

    @Test
    void givenExistingAccount_whenUpdateAndSave_thenChangesArePersisted() {
        AccountUsername username = new AccountUsername(ANY_USERNAME);
        HomePlanetName initialHomePlanetName = new HomePlanetName(ANY_HOME_PLANET_NAME);
        Account account = new Account(username, initialHomePlanetName);
        accountRepository.save(account);

        HomePlanetName newHomePlanetName = new HomePlanetName(NEW_HOME_PLANET_NAME);
        account = new Account(username, newHomePlanetName);
        accountRepository.save(account);

        Account updatedAccount = accountRepository.findByUsername(username);

        assertNotNull(updatedAccount);
        assertEquals(username, updatedAccount.getUsername());
        assertEquals(newHomePlanetName, updatedAccount.getHomePlanetName());
    }

    private void clearAccounts() {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.createQuery("delete from Account").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
