package com.impacttechs.assignment.pbxintegrationservice.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * An holder of customized error codes.
 */
@Getter
@Setter
public class ApiResponse  implements Serializable {

    private static final long serialVersionUID = -9008234528570047907L;
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
