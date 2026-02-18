package com.sorokaandriy.user_service.dto;

import jakarta.persistence.Column;

import java.math.BigDecimal;

public record UserEntityRequest(

        String name,
        String surname,
        String email,
        BigDecimal balance
) {
}
