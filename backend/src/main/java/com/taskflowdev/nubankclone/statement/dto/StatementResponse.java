package com.taskflowdev.nubankclone.statement.dto;

import com.taskflowdev.nubankclone.transaction.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record StatementResponse(
        UUID id,
        String accountEmail,
        TransactionType type,
        BigDecimal amount,
        String description,
        Instant createdAt
) {
}
