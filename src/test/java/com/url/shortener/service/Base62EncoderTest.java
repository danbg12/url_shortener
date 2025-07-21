package com.url.shortener.service;

import com.url.shortener.GeneralUrlTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(
        properties = {"test-base62.encoder.charset=ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"})
class Base62EncoderTest extends GeneralUrlTest {
    @Autowired
    private Base62Encoder base62Encoder;

    @Test
    void test_Encode_Single_Num() {
        List<Long> numbers = List.of(1L);
        List<String> result = base62Encoder.encode(numbers);

        assertEquals(1, result.size());
        assertEquals("B", result.get(0));
    }

    @Test
    void test_Encode_Multiple_Nums() {
        List<Long> numbers = List.of(1L, 62L, 125L);
        List<String> result = base62Encoder.encode(numbers);

        assertEquals(3, result.size());
        assertEquals("B", result.get(0));
        assertEquals("BA", result.get(1));
        assertEquals("CB", result.get(2));
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 10L, 62L, 100L, 1000L, 3844L, 238328L})
    void test_Encode_Various_Single_Nums(Long number) {
        List<Long> numbers = List.of(number);
        List<String> result = base62Encoder.encode(numbers);

        assertEquals(1, result.size());
        assertNotNull(result.get(0));
        assertFalse(result.get(0).isEmpty());

        assertTrue(result.get(0).chars().allMatch(c ->
                "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".indexOf(c) >= 0));
    }

    @Test
    void test_Method_Return_Empty_List_For_Null_Input() {
        List<String> result = base62Encoder.encode(null);
        assertTrue(result.isEmpty());
    }

    @Test
    void test_Return_Empty_List_For_Empty_Input() {
        List<Long> emptyList = Collections.emptyList();

        List<String> result = base62Encoder.encode(emptyList);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowExceptionForNullNumber() {
        List<Long> numbersWithNull = Arrays.asList(1L, null, 3L);

        IllegalArgumentException illegalArgumentException =
                assertThrows(IllegalArgumentException.class, () -> base62Encoder.encode(numbersWithNull));
        assertEquals("Number cannot be null", illegalArgumentException.getMessage());
    }

    @Test
    void shouldThrowExceptionForNonPositiveNumbers() {
        List<Long> numbersWithInvalid = List.of(1L, -10L, 3L);

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> base62Encoder.encode(numbersWithInvalid));
        assertEquals("Number must be non-negative, got: -10", exception.getMessage());
    }
}