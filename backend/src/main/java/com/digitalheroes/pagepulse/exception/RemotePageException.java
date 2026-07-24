package com.digitalheroes.pagepulse.exception;

import lombok.Getter;

@Getter
public class RemotePageException extends RuntimeException {

    private final int statusCode;

    public RemotePageException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public RemotePageException(int statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }
}