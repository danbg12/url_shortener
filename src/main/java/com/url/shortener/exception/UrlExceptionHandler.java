package com.url.shortener.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class UrlExceptionHandler {
    private static final DateTimeFormatter TIME_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String GENERIC_ERROR_MESSAGE = "An error occurred while processing your request";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("Validation failed: {}", e.getMessage(), e);
        Map<String, String> fieldErrors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );
        return ErrorResponse.builder()
                .message("Validation failed")
                .errors(fieldErrors)
                .timestamp(LocalDateTime.now().format(TIME_PATTERN))
                .status(HttpStatus.BAD_REQUEST.value())
                .path(getPath())
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException e) {
        log.error("Constraint violation: {}", e.getMessage(), e);
        Map<String, String> violations = new HashMap<>();
        e.getConstraintViolations().forEach(violation ->
                violations.put(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()
                )
        );
        return ErrorResponse.builder()
                .message("Validation failed")
                .errors(violations)
                .timestamp(LocalDateTime.now().format(TIME_PATTERN))
                .status(HttpStatus.BAD_REQUEST.value())
                .path(getPath())
                .build();
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleSQLException(SQLException e) {
        log.error("Database error occurred: {}", e.getMessage(), e);
        String message = isProduction()
                ? "Database operation failed"
                : "Database error: " + e.getMessage();

        return ErrorResponse.builder()
                .message(message)
                .errorCode("DB_ERROR")
                .timestamp(LocalDateTime.now().format(TIME_PATTERN))
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .path(getPath())
                .build();
    }

    @ExceptionHandler(FailedReadHashException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleFailedReadHashException(FailedReadHashException e) {
        log.warn("Failed read hash: {}", e.getMessage(), e);

        return ErrorResponse.builder()
                .message(e.getMessage())
                .errorCode("FAILED_READ_HASH")
                .timestamp(LocalDateTime.now().format(TIME_PATTERN))
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .path(getPath())
                .build();
    }

    @ExceptionHandler(FailedHashBatchException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleFailedHashBatchException(FailedHashBatchException e) {
        log.warn("Failed hash batch: {}", e.getMessage(), e);

        return ErrorResponse.builder()
                .message(e.getMessage())
                .errorCode("FAILED_HASH_BATCH")
                .timestamp(LocalDateTime.now().format(TIME_PATTERN))
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .path(getPath())
                .build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("Invalid argument: {}", e.getMessage(), e);

        return ErrorResponse.builder()
                .message(e.getMessage())
                .errorCode("INVALID_ARGUMENT")
                .timestamp(LocalDateTime.now().format(TIME_PATTERN))
                .status(HttpStatus.BAD_REQUEST.value())
                .path(getPath())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e) {
        log.error("Unexpected error occurred", e);
        String message = isProduction()
                ? GENERIC_ERROR_MESSAGE
                : e.getMessage();

        return ErrorResponse.builder()
                .message(message)
                .errorCode("INTERNAL_ERROR")
                .timestamp(LocalDateTime.now().format(TIME_PATTERN))
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .path(getPath())
                .traceId(MDC.get("traceId"))
                .build();
    }

    private String getPath() {
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getRequestURI();
    }

    private boolean isProduction() {
        return "prod".equals(System.getProperty("spring.profiles.active"));
    }
}