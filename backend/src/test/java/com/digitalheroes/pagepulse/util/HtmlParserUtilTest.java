package com.digitalheroes.pagepulse.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

class HtmlParserUtilTest {

    private final HtmlParserUtil htmlParserUtil = new HtmlParserUtil();

    @Test
    void extractMetaDescriptionReturnsEmptyWhenMissing() {
        Document document = htmlParserUtil.parse("<html><head><title>Test</title></head><body><h1>Heading</h1></body></html>", "https://example.com");

        assertEquals("", htmlParserUtil.extractMetaDescription(document));
    }

    @Test
    void countH1ReturnsZeroWhenMissing() {
        Document document = htmlParserUtil.parse("<html><body><p>No headings here</p></body></html>", "https://example.com");

        assertEquals(0, htmlParserUtil.countH1Tags(document));
    }

    @Test
    void countImagesAndAltCoverageWorks() {
        Document document = htmlParserUtil.parse("<html><body><img src='a.jpg' alt='one'><img src='b.jpg'><img src='c.jpg' alt=''></body></html>", "https://example.com");

        assertEquals(3, htmlParserUtil.countImages(document));
        assertEquals(2, htmlParserUtil.countImagesMissingAlt(document));
    }

    @Test
    void countVisibleWordsIgnoresHtml() {
        Document document = htmlParserUtil.parse("<html><body><h1>One</h1><p>Hello    beautiful    world</p></body></html>", "https://example.com");

        assertEquals(4L, htmlParserUtil.countVisibleWords(document));
    }
}