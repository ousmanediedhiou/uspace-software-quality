package uspace.infra.persistence.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import uspace.domain.account.Account;
import uspace.domain.account.AccountRepository;

public class HibernateAccountRepository implements AccountRepository {

    @Override
    public Account findByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Account.class, username);
        }
    }

    @Override
    public void save(Account account) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Account existing = session.get(Account.class, account.getUsername());
            if (existing == null) {
                session.persist(account);
            } else {
                session.merge(account);
            }
            session.flush();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Failed to persist Account entity", e);
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        return findByUsername(username) != null;
    }
}
