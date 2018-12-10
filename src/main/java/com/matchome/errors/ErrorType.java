package com.matchome.errors;

import lombok.Getter;
import lombok.Setter;

public enum ErrorType  {

        NOT_FOUND_ERROR ("notFoundError", "Error retrieving item"),
        SAVE_ERROR ("saveError", "Error saving"),
        INVALID_EMAIL ("emailValidationError", "invalid email address");

        @Getter
        @Setter
        private String type;

        @Getter
        @Setter
        private String detail;

        ErrorType(String type, String detail) {
            this.type = type;
            this.detail = detail;
        }

}
