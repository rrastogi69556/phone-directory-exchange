package com.impacttechs.assignment.pbxintegrationservice.utils;

import com.impacttechs.assignment.pbxintegrationservice.exception.PbxServiceException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;

import static com.impacttechs.assignment.pbxintegrationservice.constants.ExceptionConstants.ERROR_NOT_OK_RESPONSE;
import static com.impacttechs.assignment.pbxintegrationservice.constants.ExceptionConstants.ERROR_RESPONSE_NULL;

@Slf4j
public final class Utility {
    private Utility(){}

    public static void throwErrorIfInvalidResponse(ResponseEntity<String> responseEntity, Logger log) throws PbxServiceException {
        PbxServiceException exception = null;
        if(responseEntity == null || responseEntity.getBody() == null) {
            exception =  new PbxServiceException(ERROR_RESPONSE_NULL);
            log.error(String.format(ERROR_RESPONSE_NULL),exception);
            throw exception;
        }

        if(!responseEntity.getStatusCode().is2xxSuccessful()) {
            exception = new PbxServiceException(responseEntity.getBody());
            log.error(String.format(ERROR_NOT_OK_RESPONSE, responseEntity.getStatusCode()), exception);
            throw exception;
        }
    }
}
