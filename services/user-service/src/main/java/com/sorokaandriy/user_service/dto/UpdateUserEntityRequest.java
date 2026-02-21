package com.sorokaandriy.user_service.dto;

import com.sorokaandriy.user_service.model.RiskLevel;
import com.sorokaandriy.user_service.model.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateUserEntityRequest(
        @Size(min = 2, max = 50, message = "Name must have main 2 and max 50 symbols")
        String name,
        @Size(min = 2, max = 50, message = "Surname must have main 2 and max 50 symbols")
        String surname,
        @DecimalMin(value = "0.0", inclusive = true, message = "Balance cant be negative")
        BigDecimal balance,
        RiskLevel riskLevel,
        @PositiveOrZero(message = "Daily limits should be positive or zero")
        BigDecimal dailyLimits
) {
}
