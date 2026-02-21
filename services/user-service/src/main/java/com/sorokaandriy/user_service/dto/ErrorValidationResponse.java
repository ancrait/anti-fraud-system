package com.sorokaandriy.user_service.dto;

import java.util.Map;

public record ErrorValidationResponse(
        Map<String,String> errors
) {

}
