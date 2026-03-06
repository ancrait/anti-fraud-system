package com.sorokaandriy.transaction_service.exception;

import com.sorokaandriy.transaction_service.dto.ErrorResponseDto;
import com.sorokaandriy.transaction_service.dto.ErrorValidationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFoundException(UserNotFoundException e){

        var errorDto = new ErrorResponseDto(
                e.getMessage(),
                LocalDateTime.now());

        return ResponseEntity
                .status(404)
                .body(errorDto);
    }

    @ExceptionHandler(UserDeniedTransactionException.class)
    public ResponseEntity<ErrorResponseDto> handleUserDeniedTransactionException(UserDeniedTransactionException e){

        var errorDto = new ErrorResponseDto(
                e.getMessage(),
                LocalDateTime.now());

        return ResponseEntity
                .status(404)
                .body(errorDto);
    }

    @ExceptionHandler(TransactionLimitException.class)
    public ResponseEntity<ErrorResponseDto> handleTransactionLimitException(TransactionLimitException e){

        var errorDto = new ErrorResponseDto(
                e.getMessage(),
                LocalDateTime.now());

        return ResponseEntity
                .status(404)
                .body(errorDto);
    }

    @ExceptionHandler(TransactionalNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleTransactionalNotFoundException(TransactionalNotFoundException e){

        var errorDto = new ErrorResponseDto(
                e.getMessage(),
                LocalDateTime.now());

        return ResponseEntity
                .status(404)
                .body(errorDto);
    }

    @ExceptionHandler(NotEnoughBalance.class)
    public ResponseEntity<ErrorResponseDto> handleBalanceException(NotEnoughBalance e){

        var errorDto = new ErrorResponseDto(
                e.getMessage(),
                LocalDateTime.now());

        return ResponseEntity
                .status(404)
                .body(errorDto);
    }

    @ExceptionHandler(DailyLimitsException.class)
    public ResponseEntity<ErrorResponseDto> handleDailyLimitsException(DailyLimitsException e){

        var errorDto = new ErrorResponseDto(
                e.getMessage(),
                LocalDateTime.now());

        return ResponseEntity
                .status(404)
                .body(errorDto);
    }

    @ExceptionHandler(BalanceNotEnoughException.class)
    public ResponseEntity<ErrorResponseDto> handleBalanceException(BalanceNotEnoughException e){

        var errorDto = new ErrorResponseDto(
                e.getMessage(),
                LocalDateTime.now());

        return ResponseEntity
                .status(404)
                .body(errorDto);
    }




    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorValidationResponse> handleValidationException(MethodArgumentNotValidException e){

        Map<String,String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors()
                .forEach(error -> {
                    var fieldName = ((FieldError)error).getField();
                    var errorMessage = error.getDefaultMessage();
                    errors.put(fieldName,errorMessage);
                });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorValidationResponse(errors));
    }
}
