package com.url.shortener.cleaner;

import com.url.shortener.repository.HashRepository;
import com.url.shortener.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CleanerScheduler {
    private final UrlRepository urlRepository;
    private final HashRepository hashRepository;

    @Transactional(rollbackFor = Exception.class)
    @Scheduled(cron = "${cleaner.cron}")
    public void cleanOldUrlsAndReturnHash() {
        log.info("Starting scheduled cleanup of old URLs and returning their hash codes.");
        List<String> newFreeHashCodes = urlRepository.deleteExpiredUrlsAndReturnHashCodes();
        hashRepository.save(newFreeHashCodes.toArray(new String[0]));
        log.info("Cleanup completed. {} old URLs removed and their hash codes returned.", newFreeHashCodes.size());
    }
}
