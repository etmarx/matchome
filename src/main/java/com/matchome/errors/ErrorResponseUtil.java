package com.matchome.errors;

import com.matchome.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ErrorResponseUtil {

    /**
     * Creates an ErrorResponse and returns it as a String
     *
     * @param type       error
     * @return String errorResponse
     */
    public static String logAndGetErrorResponse(ErrorType type) {

        log.error(type.getDetail());
        return new ErrorResponse.Builder(type)
                .build()
                .toString();
    }
}
