package com.impacttechs.assignment.phonebookservice.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.impacttechs.assignment.phonebookservice.constants.ExceptionConstants.ERROR_OCCURRED_PHONEBOOK_RECORD;
import static com.impacttechs.assignment.phonebookservice.constants.ExceptionConstants.INVALID_DATA;


/*_________________________________________________
 | This class customizes & handles API error codes|
|________________________________________________| */

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(JsonProcessingException.class)
    protected ResponseEntity<ApiResponse> handleException(JsonProcessingException jpe) {
        return getApiResponseResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, jpe.getMessage());

    }

    @ExceptionHandler(PhonebookServiceException.class)
    protected ResponseEntity<ApiResponse> handleException(PhonebookServiceException jpe) {
        if(jpe.getMessage().equals(INVALID_DATA)) {
            return getApiResponseResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY, jpe.getMessage());
        } else if(jpe.getMessage().equals(ERROR_OCCURRED_PHONEBOOK_RECORD)) {
            return getApiResponseResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, jpe.getMessage());
        } else {
            return getApiResponseResponseEntity(HttpStatus.NOT_FOUND, jpe.getMessage());
        }

    }

    private ResponseEntity<ApiResponse> getApiResponseResponseEntity(HttpStatus notFound, String message) {
        ApiResponse exception = new ApiResponse(notFound);
        exception.setMessage(message);
        log.error(message);
        return buildResponseEntity(exception);
    }

    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<ApiResponse> handleException(NullPointerException jpe) {
        return getApiResponseResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, jpe.getMessage());

    }

    private ResponseEntity<ApiResponse> buildResponseEntity(ApiResponse ApiResponse) {
        return new ResponseEntity<>(ApiResponse, ApiResponse.getStatus());
    }

}
