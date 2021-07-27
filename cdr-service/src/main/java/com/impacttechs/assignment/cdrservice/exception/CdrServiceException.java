package com.impacttechs.assignment.cdrservice.exception;

public class CdrServiceException extends RuntimeException{
    private static final long serialVersionUID = 525200762386289709L;

    public CdrServiceException(String message) {
        super(message);
    }
    public CdrServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
