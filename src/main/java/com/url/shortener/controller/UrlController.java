package com.url.shortener.controller;

import com.url.shortener.model.UrlDto;
import com.url.shortener.service.UrlService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Data
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UrlController {
    private final UrlService urlService;

    @PostMapping("/shorten")
    public UrlDto shortenURL(@RequestBody @Valid UrlDto urlDto) {
        log.info("Received URL to shorten: {}", urlDto);
        return urlService.shortenURL(urlDto);
    }
}