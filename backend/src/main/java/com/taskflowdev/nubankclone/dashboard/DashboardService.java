package com.taskflowdev.nubankclone.dashboard;

import com.taskflowdev.nubankclone.account.AccountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DashboardService {
    private final AccountService accountService;

    public DashboardService(AccountService accountService) {
        this.accountService = accountService;
    }

    public BigDecimal getBalance(String email) {
        accountService.createAccount(email);
        return accountService.getBalance(email);
    }
}
