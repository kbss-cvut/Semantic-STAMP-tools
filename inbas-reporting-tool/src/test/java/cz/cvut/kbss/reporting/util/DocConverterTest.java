package cz.cvut.kbss.reporting.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DocConverterTest {

    private DocConverter converter = new DocConverter();

    @Test
    public void convertHtml2PlainTextExtractsTextFromHtmlString() {
        final String html = "<p>This is regular <b>text</b> mixed <i>with</i> <a href=\"www.inbas.cz\">HTML</a> tags.</p>";
        final String text = "This is regular text mixed with HTML tags.";

        final String result = converter.convertHtml2PlainText(html);
        assertEquals(text, result);
    }
}