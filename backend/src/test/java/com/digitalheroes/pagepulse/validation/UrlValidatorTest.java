package com.digitalheroes.pagepulse.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.digitalheroes.pagepulse.exception.InvalidURLException;
import java.net.URI;
import org.junit.jupiter.api.Test;

class UrlValidatorTest {

    private final UrlValidator urlValidator = new UrlValidator();

    @Test
    void validateAcceptsHttpsUrl() {
        URI uri = urlValidator.validate("https://openai.com");
        assertEquals("https", uri.getScheme());
    }

    @Test
    void validateRejectsLocalhost() {
        assertThrows(InvalidURLException.class, () -> urlValidator.validate("http://localhost:8080"));
    }

    @Test
    void validateRejectsFtp() {
        assertThrows(InvalidURLException.class, () -> urlValidator.validate("ftp://example.com"));
    }
}