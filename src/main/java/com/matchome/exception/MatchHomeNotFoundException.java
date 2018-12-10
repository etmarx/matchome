package com.matchome.exception;

public class MatchHomeNotFoundException extends RuntimeException {
    public MatchHomeNotFoundException(final String message) {
        super(message);
    }
}
