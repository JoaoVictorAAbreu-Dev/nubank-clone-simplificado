package com.taskflowdev.nubankclone.account;

import com.taskflowdev.nubankclone.cache.AccountCacheService;
import com.taskflowdev.nubankclone.statement.StatementEntry;
import com.taskflowdev.nubankclone.statement.StatementRepository;
import com.taskflowdev.nubankclone.transaction.TransactionType;
import com.taskflowdev.nubankclone.user.UserAccount;
import com.taskflowdev.nubankclone.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final StatementRepository statementRepository;
    private final AccountCacheService accountCacheService;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository, StatementRepository statementRepository, AccountCacheService accountCacheService) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.statementRepository = statementRepository;
        this.accountCacheService = accountCacheService;
    }

    @Transactional
    public Account createAccount(String email) {
        UserAccount owner = userRepository.findByEmail(email).orElseThrow();
        Account account = accountRepository.findByOwner_Email(email).orElseGet(() -> accountRepository.save(new Account(owner)));
        accountCacheService.cacheSummary(email, account);
        return account;
    }

    @Transactional(readOnly = true)
    public Account getByEmail(String email) {
        return accountRepository.findByOwner_Email(email).orElseThrow();
    }

    @Transactional(readOnly = true)
    public BigDecimal getBalance(String email) {
        return accountCacheService.getCachedBalance(email)
                .orElseGet(() -> {
                    Account account = getByEmail(email);
                    accountCacheService.cacheSummary(email, account);
                    return account.getBalance();
                });
    }

    @Transactional
    public Account deposit(String email, BigDecimal amount, String description) {
        validatePositiveAmount(amount);
        Account account = getByEmail(email);
        account.deposit(amount);
        statementRepository.save(new StatementEntry(account, TransactionType.DEPOSIT, amount, description, Instant.now()));
        Account saved = accountRepository.save(account);
        accountCacheService.cacheSummary(email, saved);
        return saved;
    }

    @Transactional
    public void transfer(String fromEmail, String toEmail, BigDecimal amount, String description, TransactionType type) {
        validatePositiveAmount(amount);
        if (Objects.equals(fromEmail, toEmail)) {
            throw new IllegalArgumentException("Destination account must be different from origin account");
        }
        Account from = getByEmail(fromEmail);
        Account to = getByEmail(toEmail);
        if (from.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        from.withdraw(amount);
        to.deposit(amount);
        statementRepository.save(new StatementEntry(from, type, amount, description, Instant.now()));
        statementRepository.save(new StatementEntry(to, TransactionType.RECEIVED_TRANSFER, amount, description, Instant.now()));
        accountRepository.save(from);
        accountRepository.save(to);
        accountCacheService.evict(fromEmail);
        accountCacheService.evict(toEmail);
    }

    private void validatePositiveAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }
}
