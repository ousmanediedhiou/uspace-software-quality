package uspace.domain.account;

public interface AccountRepository {
    Account findByUsername(String username);
    void save(Account account);
    boolean existsByUsername(String username);
}
