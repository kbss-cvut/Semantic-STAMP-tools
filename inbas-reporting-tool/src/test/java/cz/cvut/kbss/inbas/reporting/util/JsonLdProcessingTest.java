package cz.cvut.kbss.inbas.reporting.util;

import cz.cvut.kbss.inbas.reporting.environment.util.Environment;
import cz.cvut.kbss.inbas.reporting.exception.JsonProcessingException;
import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import cz.cvut.kbss.inbas.reporting.rest.dto.model.RawJson;
import cz.cvut.kbss.inbas.reporting.service.arms.SiraOption;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class JsonLdProcessingTest {

    private static final URI[] EXPECTED_SIRA_OPTIONS = {
            URI.create("http://onto.fel.cvut.cz/ontologies/arms/sira/model/accident-severity/catastrophic"),
            URI.create("http://onto.fel.cvut.cz/ontologies/arms/sira/model/accident-severity/major"),
            URI.create("http://onto.fel.cvut.cz/ontologies/arms/sira/model/accident-severity/minor"),
            URI.create("http://onto.fel.cvut.cz/ontologies/arms/sira/model/accident-severity/negligible")
    };

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getOrderedOptionsReadsOptionsFromJsonLdAndReturnsThemOrdered() throws Exception {
        final RawJson json = new RawJson(Environment.loadData("option/reportingPhase.json", String.class));
        final List<URI> expected = Arrays.asList(URI.create("http://onto.fel.cvut.cz/ontologies/inbas-test/first"),
                URI.create("http://onto.fel.cvut.cz/ontologies/inbas-test/second"),
                URI.create("http://onto.fel.cvut.cz/ontologies/inbas-test/third"));
        assertEquals(expected, JsonLdProcessing.getOrderedOptions(json, Vocabulary.s_p_is_higher_than));
    }

    @Test
    public void getOrderedOptionsThrowsProcessingExceptionForInvalidJson() throws Exception {
        final String invalidJson = "This is definitely not a valid JSON.";
        thrown.expect(JsonProcessingException.class);
        thrown.expectMessage("The specified JSON is not valid. JSON: " + invalidJson);
        JsonLdProcessing.getOrderedOptions(new RawJson(invalidJson), Vocabulary.s_p_is_higher_than);
    }

    @Test
    public void getOrderedOptionsThrowsProcessingExceptionWhenJsonCannotBeOrdered() throws Exception {
        final RawJson json = new RawJson(Environment.loadData("data/occurrenceWithSubEvents.json", String.class));
        thrown.expect(JsonProcessingException.class);
        thrown.expectMessage("The specified JSON does not contain options that can be sorted.");
        JsonLdProcessing.getOrderedOptions(json, Vocabulary.s_p_is_higher_than);
    }

    @Test
    public void getItemWithTypeReturnsFirstItemWithTheSpecifiedType() throws Exception {
        final URI expected = URI.create("http://onto.fel.cvut.cz/ontologies/inbas-test/second");

        final RawJson json = new RawJson(Environment.loadData("option/reportingPhase.json", String.class));
        final URI result = JsonLdProcessing.getItemWithType(json, Vocabulary.s_c_default_phase);
        assertEquals(expected, result);
    }

    @Test
    public void getItemWithTypeHandlesItemWithTypeNotAnArray() throws Exception {
        final URI expected = URI.create("http://onto.fel.cvut.cz/ontologies/inbas-test/second");
        final String json =
                "[{\"@id\": \"" + expected.toString() + "\", \"@type\": \"" + Vocabulary.s_c_default_phase + "\"}]";

        final URI result = JsonLdProcessing.getItemWithType(new RawJson(json), Vocabulary.s_c_default_phase);
        assertEquals(expected, result);
    }

    @Test
    public void getItemWithTypeReturnsNullIfNoMatchingItemIsFound() throws Exception {
        final RawJson json = new RawJson(Environment.loadData("option/reportingPhase.json", String.class));
        // Order property is definitely not it types of any of the items
        assertNull(JsonLdProcessing.getItemWithType(json, Vocabulary.s_p_is_higher_than));
    }

    @Test
    public void getItemWithTypeThrowsProcessingExceptionForInvalidJson() throws Exception {
        final String invalidJson = "This is definitely not a valid JSON.";
        thrown.expect(JsonProcessingException.class);
        thrown.expectMessage("The specified JSON is not valid. JSON: " + invalidJson);
        JsonLdProcessing.getItemWithType(new RawJson(invalidJson), Vocabulary.s_c_default_phase);
    }

    @Test
    public void readJsonLdReturnsListOfInstancesReadFromInput() throws Exception {
        final RawJson jsonLd = new RawJson(Environment.loadData("option/siraAccidentSeverity.json", String.class));
        final List<SiraOption> result = JsonLdProcessing.readFromJsonLd(jsonLd, SiraOption.class);
        assertNotNull(result);
        assertEquals(EXPECTED_SIRA_OPTIONS.length, result.size());
        final Set<URI> expected = new HashSet<>(Arrays.asList(EXPECTED_SIRA_OPTIONS));
        result.forEach(o -> assertTrue(expected.contains(o.getUri())));
    }
}
