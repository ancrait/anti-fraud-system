package com.sorokaandriy.transaction_service.dto;


import java.time.LocalDateTime;

public record ErrorResponseDto(
        String message,
        LocalDateTime errorTime
) {

}
