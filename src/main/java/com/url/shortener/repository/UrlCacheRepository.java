package com.url.shortener.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class UrlCacheRepository {
    @Value("${redis.repository.key-prefix}")
    private String key_prefix;
    @Value("${redis.repository.ttl}")
    private long default_ttl;
    private final RedisTemplate<String, String> redisTemplate;

    public void save(String shortUrl, String longUrl) {
        redisTemplate.opsForValue().set(key_prefix + shortUrl, longUrl, default_ttl, TimeUnit.HOURS);
    }

    public String findByShortUrl(String shortUrl) {
        return redisTemplate.opsForValue().get(key_prefix + shortUrl);
    }

    public Boolean exists(String shortUrl) {
        return redisTemplate.hasKey(key_prefix + shortUrl);
    }

    public Boolean delete(String shortUrl) {
        return redisTemplate.delete(key_prefix + shortUrl);
    }
}
