package com.impacttechs.assignment.phonebookservice.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * An holder of customized error codes.
 */
@Getter
@Setter
public class ApiResponse {

    private HttpStatus status;
    private String message;

    public ApiResponse(HttpStatus status) {
        this.status = status;
    }
    public ApiResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
