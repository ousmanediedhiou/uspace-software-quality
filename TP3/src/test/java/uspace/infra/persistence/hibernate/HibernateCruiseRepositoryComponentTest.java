package uspace.infra.persistence.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;

import static org.junit.jupiter.api.Assertions.*;

class HibernateCruiseRepositoryComponentTest {

    private static final String EXISTING_CRUISE_ID = "CRUISE_001";
    private static final String UNKNOWN_CRUISE_ID = "UNKNOWN_CRUISE";

    private HibernateCruiseRepository cruiseRepository;

    @BeforeEach
    void setUp() {
        cruiseRepository = new HibernateCruiseRepository();
        clearCruises();
    }

    @Test
    void givenNoCruiseInDatabase_whenFindById_thenReturnNull() {
        CruiseId cruiseId = new CruiseId(UNKNOWN_CRUISE_ID);

        Cruise result = cruiseRepository.findById(cruiseId);

        assertNull(result);
    }

    @Test
    void givenSavedCruise_whenFindById_thenReturnPersistedCruise() {
        CruiseId cruiseId = new CruiseId(EXISTING_CRUISE_ID);
        Cruise cruise = buildMinimalCruise(cruiseId);

        cruiseRepository.save(cruise);

        Cruise persistedCruise = cruiseRepository.findById(cruiseId);

        assertNotNull(persistedCruise);
        assertEquals(cruiseId, persistedCruise.getId());
    }

    @Test
    void givenExistingCruise_whenUpdateAndSave_thenCruiseIsUpdated() {
        CruiseId cruiseId = new CruiseId(EXISTING_CRUISE_ID);
        Cruise initialCruise = buildMinimalCruise(cruiseId);
        cruiseRepository.save(initialCruise);

        Cruise updatedCruise = buildMinimalCruise(cruiseId);
        cruiseRepository.save(updatedCruise);

        Cruise persistedCruise = cruiseRepository.findById(cruiseId);

        assertNotNull(persistedCruise);
        assertEquals(cruiseId, persistedCruise.getId());
    }

    private Cruise buildMinimalCruise(CruiseId cruiseId) {
        return new Cruise(
                cruiseId,
                new uspace.domain.cruise.dateTime.CruiseDateTime(),
                new uspace.domain.cruise.dateTime.CruiseDateTime(),
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    private void clearCruises() {
        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.createQuery("delete from Cruise").executeUpdate();
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
