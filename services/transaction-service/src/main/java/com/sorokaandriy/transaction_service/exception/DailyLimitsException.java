package com.sorokaandriy.transaction_service.exception;

public class DailyLimitsException extends RuntimeException {
    public DailyLimitsException(String message) {
        super(message);
    }
}
