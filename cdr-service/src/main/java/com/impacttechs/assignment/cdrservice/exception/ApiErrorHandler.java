package com.impacttechs.assignment.cdrservice.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

import static com.impacttechs.assignment.cdrservice.constants.ExceptionConstants.ERROR_OCCURRED_FETCHING_CDR_RECORD;
import static com.impacttechs.assignment.cdrservice.constants.ExceptionConstants.ERROR_OCCURRED_UPDATING_CDR_RECORD;

/*_________________________________________________
 | This class customizes & handles API error codes|
|________________________________________________| */

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class ApiErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(JsonProcessingException.class)
    protected ResponseEntity<ApiResponse> handleException(JsonProcessingException jpe) {
        return getApiErrorResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, jpe.getMessage());

    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    protected ResponseEntity<ApiResponse> handleException() {
        return getApiErrorResponseEntity(HttpStatus.BAD_REQUEST, "Missing request header 'tenant-uuid' for method parameter of type String");
    }

    @ExceptionHandler(IOException.class)
    protected ResponseEntity<ApiResponse> handleException(IOException jpe) {
        return getApiErrorResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, jpe.getMessage());

    }

    @ExceptionHandler(CdrServiceException.class)
    protected ResponseEntity<ApiResponse> handleException(CdrServiceException jpe) {
        if(jpe.getMessage().equals(ERROR_OCCURRED_UPDATING_CDR_RECORD)
                || jpe.getMessage().equals(ERROR_OCCURRED_FETCHING_CDR_RECORD)
        ) {
            return getApiErrorResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, jpe.getMessage());
        }
        return getApiErrorResponseEntity(HttpStatus.NOT_FOUND, jpe.getMessage());

    }

    private ResponseEntity<ApiResponse> getApiErrorResponseEntity(HttpStatus notFound, String message) {
        ApiResponse exception = new ApiResponse(notFound);
        exception.setMessage(message);
        log.error(message);
        return buildResponseEntity(exception);
    }

    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<ApiResponse> handleException(NullPointerException jpe) {
        return getApiErrorResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, jpe.getMessage());

    }

    private ResponseEntity<ApiResponse> buildResponseEntity(ApiResponse apiResponse) {
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

}
