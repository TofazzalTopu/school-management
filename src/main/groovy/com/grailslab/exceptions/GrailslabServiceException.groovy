package com.grailslab.exceptions

/**
 * Created by aminul on 7/11/2015.
 */
class GrailsLabServiceException extends Exception{
    public GrailsLabServiceException() {
    }

    public GrailsLabServiceException(String message) {
        super(message)
    }

    public GrailsLabServiceException(Throwable cause) {
        super(cause)
    }

    public GrailsLabServiceException(String message, Throwable cause) {
        super(message, cause)
    }

    public GrailsLabServiceException(String message, Throwable cause,
                                 boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace)
    }
}
