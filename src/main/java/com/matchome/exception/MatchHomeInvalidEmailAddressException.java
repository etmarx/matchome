package com.matchome.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MatchHomeInvalidEmailAddressException extends RuntimeException {
    public MatchHomeInvalidEmailAddressException(final String message) {
        super(message);
    }
}
