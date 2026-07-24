package com.digitalheroes.pagepulse.exception;

public class NonHtmlContentException extends RuntimeException {

    public NonHtmlContentException(String message) {
        super(message);
    }

    public NonHtmlContentException(String message, Throwable cause) {
        super(message, cause);
    }
}