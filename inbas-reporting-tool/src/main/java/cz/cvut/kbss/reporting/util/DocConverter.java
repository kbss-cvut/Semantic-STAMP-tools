package cz.cvut.kbss.reporting.util;

import org.jsoup.Jsoup;

import java.util.Objects;

/**
 * Utility class for document conversion between various format.
 */
public class DocConverter {

    /**
     * Converts HTML represented as string to plain text by removing all HTML tags and keeping only the text.
     *
     * @param html The HTML to convert
     * @return Text content of the input document
     */
    public String convertHtml2PlainText(String html) {
        Objects.requireNonNull(html);
        final org.jsoup.nodes.Document doc = Jsoup.parse(html);
        return doc.body().text();
    }
}
