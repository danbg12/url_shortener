package com.url.shortener.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class Base62Encoder {
    @Value("${base62.encoder.charset}")
    private String base62Charset;

    //FIXME COEFICIENTUL DE IMPARTIRE A NUMARULUI
    public String encode(Long number) {
        log.info("Starting Base62 encoding for number: {}", number);
        StringBuilder base62 = new StringBuilder();
        while (number > 0) {
            int remainder = (int) (number % 62);
            base62.insert(0, base62Charset.charAt(remainder));
            number /= 62;
        }
        log.info("Finished Base62 encoding number {}. Encoded value: {}", number, base62.toString());
        return base62.toString();
    }

}
