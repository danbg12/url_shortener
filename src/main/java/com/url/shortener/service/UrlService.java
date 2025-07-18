package com.url.shortener.service;

import com.url.shortener.model.UrlDto;
import com.url.shortener.repository.UrlCacheRepository;
import com.url.shortener.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlService {
    private final UrlRepository urlRepository;
    private final UrlCacheRepository urlCacheRepository;

    public UrlDto shortenURL(UrlDto urlDto) {
        return null;
    }
}