package uspace.infra.persistence.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;

import java.util.Objects;

public class HibernateCruiseRepository implements CruiseRepository {

    @Override
    public Cruise findById(CruiseId id) {
        Objects.requireNonNull(id, "id must not be null");
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Cruise.class, id);
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }

    @Override
    public void save(Cruise cruise) {
        Objects.requireNonNull(cruise, "cruise must not be null");

        Session session = null;
        Transaction tx = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            // Ne lis rien avant, on merge simplement l’agrégat complet
            session.merge(cruise);

            tx.commit(); // flush implicite avant commit
        } catch (Exception e) {
            if (tx != null && tx.getStatus() != TransactionStatus.COMMITTED) {
                try { tx.rollback(); } catch (Exception ignored) { }
            }
            throw new RuntimeException("Failed to persist Cruise entity", e);
        } finally {
            if (session != null && session.isOpen()) {
                try { session.close(); } catch (Exception ignored) { }
            }
        }
    }
}
