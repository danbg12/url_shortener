package com.url.shortener.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private String message;
    private String timestamp;
    private Integer status;
    private String errorCode;
    private String path;
    private Map<String, String> errors;
    private String traceId;
    private Integer retryAfter;
}
