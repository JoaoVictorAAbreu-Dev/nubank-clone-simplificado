package com.taskflowdev.nubankclone.account;

import com.taskflowdev.nubankclone.account.dto.AccountResponse;
import com.taskflowdev.nubankclone.account.dto.DepositRequest;
import com.taskflowdev.nubankclone.account.dto.PixRequest;
import com.taskflowdev.nubankclone.account.dto.TransferRequest;
import com.taskflowdev.nubankclone.transaction.TransactionType;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/me")
    public AccountResponse me(Authentication authentication) {
        return AccountMapper.toResponse(accountService.createAccount(authentication.getName()));
    }

    @PostMapping("/deposit")
    @ResponseStatus(HttpStatus.OK)
    public AccountResponse deposit(Authentication authentication, @Valid @RequestBody DepositRequest request) {
        Account account = accountService.deposit(authentication.getName(), request.amount(), request.description());
        return AccountMapper.toResponse(account);
    }

    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.OK)
    public void transfer(Authentication authentication, @Valid @RequestBody TransferRequest request) {
        accountService.transfer(authentication.getName(), request.toEmail(), request.amount(), request.description(), TransactionType.TRANSFER);
    }

    @PostMapping("/pix")
    @ResponseStatus(HttpStatus.OK)
    public void pix(Authentication authentication, @Valid @RequestBody PixRequest request) {
        accountService.transfer(authentication.getName(), request.toEmail(), request.amount(), request.description(), TransactionType.PIX);
    }
}
