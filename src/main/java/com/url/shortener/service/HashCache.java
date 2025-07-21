package com.url.shortener.service;

import com.url.shortener.exception.FailedReadHashException;
import com.url.shortener.repository.HashRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class HashCache {
    private final LinkedBlockingQueue<String> hashQueue;
    private final HashGenerator hashGenerator;
    private final HashRepository hashRepository;
    private final Executor executor;

    private final int cacheSize;
    private final double refillThreshold;

    private final AtomicBoolean isRefilling = new AtomicBoolean(false);

    public HashCache(
            @Value("${hash.cache.size:1000}") int cacheSize,
            @Value("${hash.cache.refill-threshold:0.2}") double refillThreshold,
            HashGenerator hashGenerator,
            HashRepository hashRepository,
            @Qualifier("customAsyncExecutor") Executor executor) {

        this.cacheSize = cacheSize;
        this.refillThreshold = refillThreshold;
        this.hashQueue = new LinkedBlockingQueue<>(cacheSize);
        this.hashGenerator = hashGenerator;
        this.hashRepository = hashRepository;
        this.executor = executor;
        log.info("HashCache initialized with size: {} and refill threshold: {}%",
                cacheSize, refillThreshold * 100);
    }

    @PostConstruct
    private void init() {
        refillCacheIfNeeded();
    }

    private boolean needsRefill() {
        double currentPercentage = (double) hashQueue.size() / cacheSize;
        return currentPercentage < refillThreshold;
    }

    public String getHash() {
        if (needsRefill()) {
            refillCacheIfNeeded();
        }
        try {
            return hashQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Interrupted while getting hash from cache", e);
            throw new FailedReadHashException("Failed to read hash from cache: " + e.getMessage());
        }
    }

    private void refillCacheIfNeeded() {
        if (!isRefilling.compareAndSet(false, true)) {
            log.debug("Refill already in progress, skipping");
            return;
        }
        executor.execute(() -> {
            try {
                executor.execute(hashGenerator::generateBatch);
                Integer toLoad = cacheSize - hashQueue.size();

                hashQueue.addAll(hashRepository.getHashBatch(toLoad));
                log.info("Cache refill completed.");
            } catch (Exception e) {
                log.error("Error during cache refill", e);
                throw new FailedReadHashException("Failed to refill hash cache: " + e.getMessage());
            } finally {
                isRefilling.set(false);
            }
        });
    }
}
