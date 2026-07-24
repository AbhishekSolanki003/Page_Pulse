package com.digitalheroes.pagepulse.validation;

import com.digitalheroes.pagepulse.exception.InvalidURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class UrlValidator {

    public URI validate(String rawUrl) {
        if (!StringUtils.hasText(rawUrl)) {
            throw new InvalidURLException("Invalid URL");
        }

        try {
            URI uri = new URI(rawUrl.trim());
            String scheme = uri.getScheme();
            String host = uri.getHost();

            if (!StringUtils.hasText(scheme) || !StringUtils.hasText(host)) {
                throw new InvalidURLException("Invalid URL");
            }

            String normalizedScheme = scheme.toLowerCase(Locale.ROOT);
            if (!"http".equals(normalizedScheme) && !"https".equals(normalizedScheme)) {
                throw new InvalidURLException("Only http and https URLs are supported");
            }

            String normalizedHost = host.toLowerCase(Locale.ROOT);
            if (normalizedHost.equals("localhost") || normalizedHost.endsWith(".localhost") || normalizedHost.equals("127.0.0.1") || normalizedHost.equals("::1")) {
                throw new InvalidURLException("Localhost URLs are not allowed");
            }

            return uri;
        } catch (URISyntaxException | IllegalArgumentException ex) {
            throw new InvalidURLException("Invalid URL", ex);
        }
    }
}