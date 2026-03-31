package com.aditi_final.bridgeskill_api.exception;

public class ForbiddenActionException extends RuntimeException {
    public ForbiddenActionException(String message) {
        super(message); // It sends that message to the parent class RuntimeException. So, inside GlobalExceptionHandler, Spring can read that same message.
    }
}
// ForbiddenActionException = describes the error