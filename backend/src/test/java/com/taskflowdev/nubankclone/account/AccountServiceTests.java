package com.taskflowdev.nubankclone.account;

import com.taskflowdev.nubankclone.statement.StatementRepository;
import com.taskflowdev.nubankclone.user.UserAccount;
import com.taskflowdev.nubankclone.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
class AccountServiceTests {

    @Autowired
    private AccountService accountService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private StatementRepository statementRepository;

    @Test
    void depositUpdatesBalance() {
        var user = new UserAccount("user@example.com", "encoded");
        var account = new Account(user);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(accountRepository.findByOwner_Email("user@example.com")).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);

        var result = accountService.deposit("user@example.com", BigDecimal.valueOf(100), "deposit");

        assertEquals(0, BigDecimal.valueOf(100).compareTo(result.getBalance()));
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
                accountService.transfer("user@example.com", "friend@example.com", BigDecimal.ONE, "transfer", com.taskflowdev.nubankclone.transaction.TransactionType.TRANSFER));
    }
}
