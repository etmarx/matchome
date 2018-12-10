package com.matchome.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matchome.errors.ErrorType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private String type;
    private String detail;

    public static class Builder {
        private String type;
        private String detail;

        public Builder(ErrorType errorType) {
            this.type = errorType.getType();
            this.detail = errorType.getDetail();
        }


        public ErrorResponse build(){
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.type = this.type;
            errorResponse.detail = this.detail;

            return errorResponse;
        }

    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error("unable to serialize error response: {}", this);
            return super.toString();
        }
    }
}
