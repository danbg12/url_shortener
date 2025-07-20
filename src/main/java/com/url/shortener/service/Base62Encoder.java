package com.url.shortener.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class Base62Encoder {
    @Value("${base62.encoder.charset}")
    private String base62Charset;
    private static final Integer BASE = 62;

    public List<String> encode(List<Long> numbers) {
        log.info("Starting Base62 encoding for list of numbers.");
        if(numbers == null || numbers.isEmpty()) {
            log.warn("List of numbers is null or empty. Returning empty list.");
            return Collections.emptyList();
        }

        return numbers.stream()
                .map(this::encodeSingleNum)
                .collect(Collectors.toList());
    }

    private String encodeSingleNum(Long number) {
        log.info("Starting Base62 encoding for number: {}", number);
        if (number == null) {
            log.error("Number is null. Cannot perform Base62 encoding.");
            throw new IllegalArgumentException("Number cannot be null");
        }

        if (number <= 0) {
            log.error("Number must be non-negative and not 0, got: {}", number);
            throw new IllegalArgumentException("Number must be non-negative, got: " + number);
        }

        StringBuilder result = new StringBuilder();
        Long tempNumber = number;
        while (tempNumber > 0) {
            result.insert(0, base62Charset.charAt((int) (tempNumber % BASE)));
            tempNumber /= BASE;
        }
        log.info("Base62 encoding completed for number: {}. Result: {}", number, result);
        return result.toString();
    }
}
