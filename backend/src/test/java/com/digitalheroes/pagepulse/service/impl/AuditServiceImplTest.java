package com.digitalheroes.pagepulse.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.digitalheroes.pagepulse.dto.AuditRequest;
import com.digitalheroes.pagepulse.dto.AuditResponse;
import com.digitalheroes.pagepulse.exception.NonHtmlContentException;
import java.net.http.HttpTimeoutException;
import com.digitalheroes.pagepulse.util.HtmlParserUtil;
import com.digitalheroes.pagepulse.validation.UrlValidator;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.util.List;
import java.util.Map;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuditServiceImplTest {

    private HttpClient httpClient;
    private AuditServiceImpl auditService;
    private final UrlValidator urlValidator = new UrlValidator();
    private final HtmlParserUtil htmlParserUtil = new HtmlParserUtil();

    @BeforeEach
    void setUp() {
        httpClient = mock(HttpClient.class);
        auditService = new AuditServiceImpl(httpClient, urlValidator, htmlParserUtil);
    }

    @Test
    void auditHappyPathBuildsResponse() throws Exception {
        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn("<html><head><title>OpenAI</title><meta name='description' content='Creating safe AGI'></head><body><h1>One</h1><h1>Two</h1><img src='a.jpg'><img src='b.jpg' alt='desc'><p>Hello world here</p></body></html>");
        when(response.uri()).thenReturn(URI.create("https://openai.com"));
        when(response.headers()).thenReturn(HttpHeaders.of(Map.of("content-type", List.of("text/html; charset=UTF-8")), (k, v) -> true));
        when(httpClient.send(any(HttpRequest.class), anyBodyHandler())).thenReturn(response);

        AuditResponse auditResponse = auditService.audit(new AuditRequest("https://openai.com"));

        assertEquals(200, auditResponse.getStatus());
        assertEquals("OpenAI", auditResponse.getTitle());
        assertEquals("Creating safe AGI", auditResponse.getMetaDescription());
        assertEquals(2, auditResponse.getH1Count());
        assertEquals(1, auditResponse.getMissingAltImages());
        assertEquals(3L, auditResponse.getWordCount());
    }

    @Test
    void auditRejectsNonHtmlContent() throws Exception {
        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn("{\"ok\":true}");
        when(response.uri()).thenReturn(URI.create("https://openai.com"));
        when(response.headers()).thenReturn(HttpHeaders.of(Map.of("content-type", List.of("application/json")), (k, v) -> true));
        when(httpClient.send(any(HttpRequest.class), anyBodyHandler())).thenReturn(response);

        assertThrows(NonHtmlContentException.class, () -> auditService.audit(new AuditRequest("https://openai.com")));
    }

    @Test
    void auditMapsTimeoutToTimeoutException() throws Exception {
        when(httpClient.send(any(HttpRequest.class), anyBodyHandler())).thenThrow(new HttpTimeoutException("timeout"));

        assertThrows(com.digitalheroes.pagepulse.exception.TimeoutException.class, () -> auditService.audit(new AuditRequest("https://openai.com")));
    }

    @Test
    void htmlParserCountsVisibleWordsAndImages() {
        Document document = htmlParserUtil.parse("<html><body><h1>Title</h1><img src='a.jpg'><img src='b.jpg' alt='ok'><p>Hello beautiful world</p></body></html>", "https://example.com");

        assertEquals(1, htmlParserUtil.countH1Tags(document));
        assertEquals(2, htmlParserUtil.countImages(document));
        assertEquals(1, htmlParserUtil.countImagesMissingAlt(document));
        assertEquals(3L, htmlParserUtil.countVisibleWords(document));
    }

    @SuppressWarnings("unchecked")
    private BodyHandler<String> anyBodyHandler() {
        return (BodyHandler<String>) any(BodyHandler.class);
    }
}