package com.taskflowdev.nubankclone.statement;

import com.taskflowdev.nubankclone.account.AccountService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/statements")
public class StatementController {
    private final AccountService accountService;
    private final StatementRepository statementRepository;

    public StatementController(AccountService accountService, StatementRepository statementRepository) {
        this.accountService = accountService;
        this.statementRepository = statementRepository;
    }

    @GetMapping
    public List<StatementEntry> list(Authentication authentication) {
        accountService.createAccount(authentication.getName());
        return statementRepository.findAll();
    }
}
