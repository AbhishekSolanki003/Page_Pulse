package com.digitalheroes.pagepulse.service.impl;

import com.digitalheroes.pagepulse.dto.AuditRequest;
import com.digitalheroes.pagepulse.dto.AuditResponse;
import com.digitalheroes.pagepulse.exception.InvalidURLException;
import com.digitalheroes.pagepulse.exception.NonHtmlContentException;
import com.digitalheroes.pagepulse.exception.RemotePageException;
import com.digitalheroes.pagepulse.exception.TimeoutException;
import com.digitalheroes.pagepulse.service.AuditService;
import com.digitalheroes.pagepulse.util.HtmlParserUtil;
import com.digitalheroes.pagepulse.validation.UrlValidator;
import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.URI;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Locale;
import javax.net.ssl.SSLException;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(15);

    private final HttpClient httpClient;
    private final UrlValidator urlValidator;
    private final HtmlParserUtil htmlParserUtil;

    @Override
    public AuditResponse audit(AuditRequest request) {
        URI targetUri = urlValidator.validate(request.getUrl());
        HttpRequest httpRequest = HttpRequest.newBuilder(targetUri)
                .GET()
                .timeout(REQUEST_TIMEOUT)
                .header("Accept", "text/html,application/xhtml+xml")
                .header("User-Agent", "PagePulse/1.0")
                .build();

        long startNanos = System.nanoTime();
        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            long responseTime = toMillis(System.nanoTime() - startNanos);
            int statusCode = response.statusCode();
            String contentType = contentType(response.headers());

            if (statusCode >= 400) {
                throw new RemotePageException(statusCode, "Target page returned HTTP " + statusCode);
            }

            if (!isHtml(contentType)) {
                throw new NonHtmlContentException("Target URL did not return HTML content");
            }

            Document document = htmlParserUtil.parse(response.body(), response.uri().toString());

            return AuditResponse.builder()
                    .status(statusCode)
                    .responseTime(responseTime)
                    .title(htmlParserUtil.extractTitle(document))
                    .metaDescription(htmlParserUtil.extractMetaDescription(document))
                    .h1Count(htmlParserUtil.countH1Tags(document))
                    .missingAltImages(htmlParserUtil.countImagesMissingAlt(document))
                    .wordCount(htmlParserUtil.countVisibleWords(document))
                    .build();
        } catch (HttpTimeoutException ex) {
            throw new TimeoutException("Request timed out while fetching the target URL", ex);
        } catch (UnknownHostException ex) {
            throw new InvalidURLException("DNS failure: unable to resolve host", ex);
        } catch (ConnectException ex) {
            throw new RemotePageException(502, "Connection refused by target server", ex);
        } catch (NoRouteToHostException ex) {
            throw new RemotePageException(502, "No route to host", ex);
        } catch (SSLException ex) {
            throw new RemotePageException(502, "SSL handshake failed", ex);
        } catch (IOException ex) {
            Throwable rootCause = rootCause(ex);
            if (rootCause instanceof UnknownHostException) {
                throw new InvalidURLException("DNS failure: unable to resolve host", ex);
            }
            if (rootCause instanceof ConnectException) {
                throw new RemotePageException(502, "Connection refused by target server", ex);
            }
            if (rootCause instanceof NoRouteToHostException) {
                throw new RemotePageException(502, "No route to host", ex);
            }
            if (rootCause instanceof SSLException) {
                throw new RemotePageException(502, "SSL handshake failed", ex);
            }
            if (isRedirectLoop(ex)) {
                throw new RemotePageException(508, "Redirect loop detected", ex);
            }
            throw new RemotePageException(502, "Failed to fetch the target URL", ex);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new TimeoutException("Request interrupted while fetching the target URL", ex);
        }
    }

    private long toMillis(long nanos) {
        return Math.max(1L, Math.round(nanos / 1_000_000.0));
    }

    private String contentType(HttpHeaders headers) {
        return headers.firstValue("content-type")
                .orElse("")
                .toLowerCase(Locale.ROOT);
    }

    private boolean isHtml(String contentType) {
        return contentType.contains("text/html");
    }

    private boolean isRedirectLoop(IOException ex) {
        String message = ex.getMessage();
        return message != null && message.toLowerCase(Locale.ROOT).contains("redirect");
    }

    private Throwable rootCause(Throwable throwable) {
        Throwable current = throwable;
        while (current.getCause() != null && current.getCause() != current) {
            current = current.getCause();
        }
        return current;
    }
}