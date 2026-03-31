package com.aditi_final.bridgeskill_api.dto.common;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
public class ApiErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private Map<String, String> errors;
}

// ApiErrorResponse = shape of the JSON response: This file is just the response model. It defines what your error JSON should look like: