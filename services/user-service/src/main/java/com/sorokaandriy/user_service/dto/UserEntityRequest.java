package com.sorokaandriy.user_service.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record UserEntityRequest(
        @NotBlank(message = "User name cannot be empty")
        String name,
        @NotBlank(message = "User id cannot be empty")
        String surname,
        @NotBlank(message = "User id cannot be empty")
        String email,
        @DecimalMin(value = "0.0", inclusive = true, message = "Balance should not be negative")
        BigDecimal balance
) {
}
