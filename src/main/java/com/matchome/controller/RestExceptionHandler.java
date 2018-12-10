package com.matchome.controller;

import com.matchome.exception.MatchHomeNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<String> handleBadRequestException(Exception e) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/problem+json")
                .body(e.getMessage());
    }

    @ExceptionHandler({MatchHomeNotFoundException.class})
    public ResponseEntity<String> handleNotFoundException(Exception e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .header("Content-Type", "application/problem+json")
                .body(e.getMessage());
    }

    @ExceptionHandler({DataAccessException.class})
    public ResponseEntity<String> handleDataException(Exception e) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("Content-Type", "application/problem+json")
                .body(e.getMessage());
    }

}
