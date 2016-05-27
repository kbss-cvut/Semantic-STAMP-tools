package cz.cvut.kbss.inbas.reporting.util;

import cz.cvut.kbss.inbas.reporting.environment.util.Environment;
import cz.cvut.kbss.inbas.reporting.exception.JsonProcessingException;
import cz.cvut.kbss.inbas.reporting.rest.dto.model.RawJson;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class JsonLdProcessingTest {

    private static final String ORDER_PROPERTY = "http://onto.fel.cvut.cz/ontologies/aviation-safety/is-higher-than";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getOrderedOptionsReadsOptionsFromJsonLdAndReturnsThemOrdered() throws Exception {
        final RawJson json = new RawJson(Environment.loadData("option/reportingPhase.json", String.class));
        final List<URI> expected = Arrays.asList(URI.create("http://onto.fel.cvut.cz/ontologies/inbas-test/first"),
                URI.create("http://onto.fel.cvut.cz/ontologies/inbas-test/second"),
                URI.create("http://onto.fel.cvut.cz/ontologies/inbas-test/third"));
        assertEquals(expected, JsonLdProcessing.getOrderedOptions(json, ORDER_PROPERTY));
    }

    @Test
    public void getOrderedOptionsThrowsProcessingExceptionWhenInvalidJsonIsPassedIn() throws Exception {
        final String invalidJson = "This is definitely not a valid JSON.";
        thrown.expect(JsonProcessingException.class);
        thrown.expectMessage("The specified JSON is not valid. JSON: " + invalidJson);
        JsonLdProcessing.getOrderedOptions(new RawJson(invalidJson), ORDER_PROPERTY);
    }

    @Test
    public void getOrderedOptionsThrowsProcessingExceptionWhenJsonCannotBeOrdered() throws Exception {
        final RawJson json = new RawJson(Environment.loadData("data/occurrenceWithSubEvents.json", String.class));
        thrown.expect(JsonProcessingException.class);
        thrown.expectMessage("The specified JSON does not contain options that can be sorted.");
        JsonLdProcessing.getOrderedOptions(json, ORDER_PROPERTY);
    }
}