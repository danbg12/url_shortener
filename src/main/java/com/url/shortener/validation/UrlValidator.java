package com.url.shortener.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;

@Slf4j
public class UrlValidator implements ConstraintValidator<ValidUrl, String> {
    @Override
    public boolean isValid(String url, ConstraintValidatorContext context) {
        log.info("Checking if URL is valid: {}", url);
        url = url.trim();
        if (url == null || url.isEmpty()) {
            return false;
        }

        try {
            URL urlObj = new URL(url);

            String protocol = urlObj.getProtocol().toLowerCase();
            if (!protocol.equals("http") && !protocol.equals("https")) {
                return false;
            }

            String host = urlObj.getHost().trim();
            if (host == null || host.isEmpty()) {
                return false;
            }

            if (host.equals("localhost") || host.equals("127.0.0.1")) {
                return false;
            }

            if (url.length() < 10 || url.length() > 2048) {
                return false;
            }
            log.info("URL is valid: {}", url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
