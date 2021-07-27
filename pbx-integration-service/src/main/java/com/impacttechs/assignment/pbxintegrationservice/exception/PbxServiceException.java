package com.impacttechs.assignment.pbxintegrationservice.exception;

public class PbxServiceException extends RuntimeException{
    private static final long serialVersionUID = 525200762386289709L;

    public PbxServiceException(String message) {
        super(message);
    }
    public PbxServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
