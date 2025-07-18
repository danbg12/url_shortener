package com.url.shortener.model;

import com.url.shortener.validation.ValidUrl;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlDto {
    private Long id;
    @ValidUrl
    @NotBlank(message = "URL must not be blank")
    @Size(min = 30, max = 2048, message = "URL must be between 30 and 2048 characters")
    private String originalUrl;
    private String shortUrl;
    private LocalDateTime expiresAt;
}