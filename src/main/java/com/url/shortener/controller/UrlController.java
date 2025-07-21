package com.url.shortener.controller;

import com.url.shortener.service.UrlService;
import com.url.shortener.validation.ValidUrl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Slf4j
@Data
@RestController
@Validated
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UrlController {
    private final UrlService urlService;

    @PostMapping("/shorten/{originalUrl}")
    public String shortenURL(@PathVariable @ValidUrl String originalUrl) {
        log.info("Received URL to shorten: {}", originalUrl);
        return urlService.shortenURL(originalUrl);
    }

    @GetMapping("/redirect/{shortUrl}")
    public ResponseEntity<Void> getRedirectToOriginalUrl(@PathVariable @ValidUrl String shortUrl) {
        log.info("Received URL to retrieve short URL: {}", shortUrl);
        String originalUrlForRedirect = urlService.getShortUrl(shortUrl);
        log.info("Redirecting to original URL: {}", originalUrlForRedirect);
        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .location(URI.create(originalUrlForRedirect))
                .build();
    }
}