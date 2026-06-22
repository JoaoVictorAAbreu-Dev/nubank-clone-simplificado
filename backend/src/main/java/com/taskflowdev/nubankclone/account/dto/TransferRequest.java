package com.taskflowdev.nubankclone.account.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record TransferRequest(
        @Email String toEmail,
        @NotBlank String description,
        @DecimalMin("0.01") BigDecimal amount
) {
}
