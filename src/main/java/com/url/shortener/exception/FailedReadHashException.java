package com.url.shortener.exception;

public class FailedReadHashException extends RuntimeException {
    public FailedReadHashException(String message) {
        super(message);
    }
}
