package com.impacttechs.assignment.pbxintegrationservice.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;


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

    @ExceptionHandler(IOException.class)
    protected ResponseEntity<ApiResponse> handleException(IOException jpe) {
        return getApiErrorResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, jpe.getMessage());

    }

    @ExceptionHandler(PbxServiceException.class)
    protected ResponseEntity<ApiResponse> handleException(PbxServiceException jpe) {

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
