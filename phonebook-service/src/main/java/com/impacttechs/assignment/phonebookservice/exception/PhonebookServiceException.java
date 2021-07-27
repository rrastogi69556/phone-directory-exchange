package com.impacttechs.assignment.phonebookservice.exception;

public class PhonebookServiceException extends RuntimeException{
    private static final long serialVersionUID = 525200762386289709L;

    public PhonebookServiceException(String message) {
        super(message);
    }
    public PhonebookServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
