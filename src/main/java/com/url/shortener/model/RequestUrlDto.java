package com.url.shortener.model;

import com.url.shortener.validation.ValidUrl;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestUrlDto {
    @ValidUrl
    @NotBlank(message = "URL must not be blank")
    @Size(min = 15, max = 2048, message = "URL must be between 30 and 2048 characters")
    private String url;
}