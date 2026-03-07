package com.sorolaandriy.notification_service.dto;


import java.time.LocalDateTime;

public record ErrorResponseDto(
        String message,
        LocalDateTime errorTime
) {

}
