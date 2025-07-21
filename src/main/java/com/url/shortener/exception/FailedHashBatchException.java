package com.url.shortener.exception;

public class FailedHashBatchException extends RuntimeException {
    public FailedHashBatchException(String message) {
        super(message);
    }
}
