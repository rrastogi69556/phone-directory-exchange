package com.impacttechs.assignment.cdrservice.utils;

import com.impacttechs.assignment.cdrservice.exception.CdrServiceException;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;

import static com.impacttechs.assignment.cdrservice.constants.ExceptionConstants.ERROR_NOT_OK_RESPONSE;
import static com.impacttechs.assignment.cdrservice.constants.ExceptionConstants.ERROR_RESPONSE_NULL;

public final class Utility {
    private Utility(){}

    public static void throwErrorIfInvalidResponse(ResponseEntity<String> responseEntity, Logger log) throws CdrServiceException {
        CdrServiceException exception = null;
        if(responseEntity == null || !responseEntity.hasBody() || responseEntity.getBody() == null) {
            exception =  new CdrServiceException(ERROR_RESPONSE_NULL);
            log.error(String.format(ERROR_RESPONSE_NULL),exception);
            throw exception;
        }

        if(!responseEntity.getStatusCode().is2xxSuccessful()) {
            exception = new CdrServiceException(responseEntity.getBody());
            log.error(String.format(ERROR_NOT_OK_RESPONSE, responseEntity.getStatusCode()), exception);
            throw exception;
        }
    }
}
