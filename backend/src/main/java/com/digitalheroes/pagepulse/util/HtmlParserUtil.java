package com.digitalheroes.pagepulse.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class HtmlParserUtil {

    public Document parse(String html, String baseUri) {
        return Jsoup.parse(html == null ? "" : html, baseUri == null ? "" : baseUri);
    }

    public String extractTitle(Document document) {
        String title = document.title();
        return title == null ? "" : title.trim();
    }

    public String extractMetaDescription(Document document) {
        Element metaDescription = document.selectFirst("meta[name=description]");
        if (metaDescription == null) {
            return "";
        }
        String description = metaDescription.attr("content");
        return description == null ? "" : description.trim();
    }

    public int countH1Tags(Document document) {
        return document.select("h1").size();
    }

    public int countImages(Document document) {
        return document.select("img").size();
    }

    public int countImagesMissingAlt(Document document) {
        int missingAltImages = 0;
        Elements images = document.select("img");
        for (Element image : images) {
            String alt = image.hasAttr("alt") ? image.attr("alt") : "";
            if (alt.trim().isEmpty()) {
                missingAltImages++;
            }
        }
        return missingAltImages;
    }

    public long countVisibleWords(Document document) {
        if (document.body() == null) {
            return 0L;
        }

        String visibleText = document.body().text().trim();
        if (visibleText.isEmpty()) {
            return 0L;
        }

        String normalized = visibleText.replaceAll("\\s+", " ").trim();
        if (normalized.isEmpty()) {
            return 0L;
        }

        return normalized.split(" ").length;
    }
}