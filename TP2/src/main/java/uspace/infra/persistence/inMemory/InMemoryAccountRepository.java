package uspace.infra.persistence.inMemory;

import uspace.domain.account.Account;
import uspace.domain.account.AccountRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAccountRepository implements AccountRepository {

    private final Map<String, Account> store = new ConcurrentHashMap<>();

    @Override
    public Account findByUsername(String username) {
        return store.get(username);
    }

    @Override
    public void save(Account account) {
        store.put(account.getUsername(), account);
    }

    @Override
    public boolean existsByUsername(String username) {
        return store.containsKey(username);
    }
}
