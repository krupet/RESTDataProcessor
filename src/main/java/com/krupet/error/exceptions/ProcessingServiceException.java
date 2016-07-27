package com.krupet.error.exceptions;

/**
 * The class ProcessingServiceException.
 */
public class ProcessingServiceException extends Exception {

    private ServiceErrorCode errorCode;

    public ProcessingServiceException() {
        super();
    }

    public ProcessingServiceException(String message, ServiceErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ServiceErrorCode getErrorCode() {
        return errorCode;
    }
}
