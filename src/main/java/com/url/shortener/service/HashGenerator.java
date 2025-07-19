package com.url.shortener.service;

import com.url.shortener.repository.HashRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class HashGenerator {
    @Value("${hash.batch.size}")
    private Integer batchSize;
    private final HashRepository hashRepository;
    private final Base62Encoder base62Encoder;

    @Async()
    @Transactional
    public void generateBatch() {
        log.info("Starting batch generation of unique hash codes.");
        List<Long> uniqueNumbers = hashRepository.getUniqueNumbersBatch(batchSize);
        List<String> newFreeHashCodes = new ArrayList<>();
        for (Long uniqueNumber : uniqueNumbers) {
            newFreeHashCodes.add(base62Encoder.encode(uniqueNumber));
        }
        hashRepository.saveBatch(newFreeHashCodes.toArray(new String[0]));
        log.info("Successfully generated and saved {} new hash codes.", newFreeHashCodes.size());
    }
}
