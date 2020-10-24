package com.grailslab.exceptions

/**
 * Created by aminul on 7/11/2015.
 */
class FileUploadException extends RuntimeException{
    public FileUploadException(Throwable t) {
        super(t)
    }

    public FileUploadException(String message) {
        super(message)
    }
}
