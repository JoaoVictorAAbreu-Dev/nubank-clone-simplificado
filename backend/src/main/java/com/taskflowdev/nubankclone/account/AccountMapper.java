package com.taskflowdev.nubankclone.account;

import com.taskflowdev.nubankclone.account.dto.AccountResponse;

public final class AccountMapper {
    private AccountMapper() {
    }

    public static AccountResponse toResponse(Account account) {
        return new AccountResponse(account.getId(), account.getOwner().getEmail(), account.getBalance());
    }
}
