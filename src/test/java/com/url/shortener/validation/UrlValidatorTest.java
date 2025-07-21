package com.url.shortener.validation;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class UrlValidatorTest {
    private static final UrlValidator URL_VALIDATOR = new UrlValidator();

    @ParameterizedTest
    @ValueSource(strings = {
            "http://example.com",
            "https://example.com",
            "https://github.com/danbg12/url_shortener"
    })
    void test_IsValid_With_Valid_Url(String url) {
        assertTrue(URL_VALIDATOR.isValid(url, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "htt://example.com",
            "https:example.com",
            "http://"
    })
    void test_IsValid_With_Not_Valid_Url(String url) {
        assertFalse(URL_VALIDATOR.isValid(url, null));
    }
}