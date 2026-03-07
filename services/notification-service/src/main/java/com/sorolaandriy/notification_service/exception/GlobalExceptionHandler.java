package com.sorolaandriy.notification_service.exception;

import com.sorolaandriy.notification_service.dto.ErrorResponseDto;
import com.sorolaandriy.notification_service.dto.ErrorValidationResponse;
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

    @ExceptionHandler(TokenTimeOutException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFoundException(TokenTimeOutException e){

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
