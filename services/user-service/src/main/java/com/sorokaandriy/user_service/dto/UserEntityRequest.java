package com.sorokaandriy.user_service.dto;

import com.sorokaandriy.user_service.model.RiskLevel;
import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UserEntityRequest(
        @NotBlank(message = "User name cannot be empty")
        String name,
        @NotBlank(message = "Surname cannot be empty")
        String surname,
        @NotBlank(message = "Email cannot be empty")
        String email,
        @DecimalMin(value = "0.0", inclusive = true, message = "Balance should not be negative")
        BigDecimal balance,
        @NotNull(message = "Risk level is required")
        RiskLevel riskLevel

) {
}
