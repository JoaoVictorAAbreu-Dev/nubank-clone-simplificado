package com.taskflowdev.nubankclone.account;

import com.taskflowdev.nubankclone.cache.AccountCacheService;
import com.taskflowdev.nubankclone.statement.StatementRepository;
import com.taskflowdev.nubankclone.transaction.TransactionType;
import com.taskflowdev.nubankclone.user.UserAccount;
import com.taskflowdev.nubankclone.user.UserRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTests {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private StatementRepository statementRepository;
    private FakeAccountCacheService accountCacheService;

    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountCacheService = new FakeAccountCacheService();
        accountService = new AccountService(accountRepository, userRepository, statementRepository, accountCacheService);
    }

    @Test
    void depositUpdatesBalance() {
        var user = new UserAccount("user@example.com", "encoded");
        var account = new Account(user);
        when(accountRepository.findByOwner_Email("user@example.com")).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);

        var result = accountService.deposit("user@example.com", BigDecimal.valueOf(100), "deposit");

        assertEquals(0, BigDecimal.valueOf(100).compareTo(result.getBalance()));
    }

    @Test
    void getBalanceUsesCachedValueWhenAvailable() {
        accountCacheService.cachedBalance = BigDecimal.valueOf(250);

        BigDecimal balance = accountService.getBalance("user@example.com");

        assertEquals(0, BigDecimal.valueOf(250).compareTo(balance));
    }

    @Test
    void transferRejectsSameOriginAndDestination() {
        assertThrows(IllegalArgumentException.class, () ->
                accountService.transfer("user@example.com", "user@example.com", BigDecimal.ONE, "transfer", TransactionType.TRANSFER));
    }

    @Test
    void depositRejectsInvalidAmount() {
        assertThrows(IllegalArgumentException.class, () ->
                accountService.deposit("user@example.com", BigDecimal.ZERO, "deposit"));
    }

    @Test
    void transferRejectsInsufficientBalance() {
        var user = new UserAccount("user@example.com", "encoded");
        var target = new UserAccount("friend@example.com", "encoded");
        var account = new Account(user);
        var targetAccount = new Account(target);
        when(accountRepository.findByOwner_Email("user@example.com")).thenReturn(Optional.of(account));
        when(accountRepository.findByOwner_Email("friend@example.com")).thenReturn(Optional.of(targetAccount));

        assertThrows(IllegalArgumentException.class, () ->
                accountService.transfer("user@example.com", "friend@example.com", BigDecimal.ONE, "transfer", TransactionType.TRANSFER));
    }

    private static class FakeAccountCacheService extends AccountCacheService {
        private BigDecimal cachedBalance;

        FakeAccountCacheService() {
            super(new ObjectProvider<>() {
                @Override
                public StringRedisTemplate getObject(Object... args) {
                    return null;
                }

                @Override
                public StringRedisTemplate getIfAvailable() {
                    return null;
                }

                @Override
                public StringRedisTemplate getIfUnique() {
                    return null;
                }

                @Override
                public StringRedisTemplate getObject() {
                    return null;
                }
            });
        }

        @Override
        public Optional<BigDecimal> getCachedBalance(String email) {
            return Optional.ofNullable(cachedBalance);
        }
    }
}
