package com.sorokaandriy.transaction_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record TransactionRequest(
        @NotBlank(message = "User id cannot be empty")
        String userId,
        @Positive(message = "Amount should be positive")
        @DecimalMin(value = "0.01", message = "Minimum transaction amount is 0.01")
        double amount
) {
}
