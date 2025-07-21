package com.url.shortener.service;

import com.url.shortener.exception.FailedReadHashException;
import com.url.shortener.model.Url;
import com.url.shortener.repository.UrlCacheRepository;
import com.url.shortener.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlService {
    @Value("${short.url.lifetime}")
    private Long shortUrlLifetime;
    @Value("${short.url.prefix}")
    private String shortUrlPrefix;
    private final UrlRepository urlRepository;
    private final UrlCacheRepository urlCacheRepository;
    private final HashCache hashCache;

    public String shortenURL(String originalUrl) {
        log.info("Shortening URL: {}", originalUrl);
        String hash = hashCache.getHash();
        String shortUrlWithHash = shortUrlPrefix + hash;

        log.info("Create new URL entity with short URL: {}", shortUrlWithHash);
        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setShortUrl(shortUrlWithHash);
        url.setExpiresAt(LocalDateTime.now().plusDays(shortUrlLifetime));
        url.setUrlHashCode(hash);

        urlCacheRepository.save(shortUrlWithHash, originalUrl);
        log.info("Url saved in cache with short URL: {}", shortUrlWithHash);
        return urlRepository.save(url).getShortUrl();
    }

    public String getShortUrl(String shortUrl) {
        log.info("Searching for short URL: {}", shortUrl);
        if (urlCacheRepository.exists(shortUrl)) {
            log.info("Short URL found in cache: {}", shortUrl);
            return urlCacheRepository.findByShortUrl(shortUrl);
        }

        Url url = urlRepository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new FailedReadHashException("Url not found in repository: " + shortUrl));
        return url.getOriginalUrl();
    }
}