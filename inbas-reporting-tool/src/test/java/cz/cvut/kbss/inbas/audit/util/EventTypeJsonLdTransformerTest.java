package cz.cvut.kbss.inbas.audit.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EventTypeJsonLdTransformerTest {

    private static final String JSON_LD = "[ {\n" +
            "  \"@id\" : \"http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-1-31-26-27-488-10000000-11000000\",\n" +
            "  \"@type\" : [ \"http://onto.fel.cvut.cz/ontologies/eccairs/model/descriptive-factor\" ],\n" +
            "  \"http://www.w3.org/2000/01/rdf-schema#comment\" : [ {\n" +
            "    \"@value\" : \"The aircraft, its systems and components.\"\n" +
            "  } ],\n" +
            "  \"http://www.w3.org/2000/01/rdf-schema#label\" : [ {\n" +
            "    \"@value\" : \"11000000 - Aircraft components and systems\"\n" +
            "  } ]\n" +
            "}, {\n" +
            "  \"@id\" : \"http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-1-31-26-27-488-10000000-11000000-100000219\",\n" +
            "  \"@type\" : [ \"http://onto.fel.cvut.cz/ontologies/eccairs/model/descriptive-factor\" ],\n" +
            "  \"http://www.w3.org/2000/01/rdf-schema#comment\" : [ {\n" +
            "    \"@value\" : \"(ATA Code 0500) Manufacturers' recommended time limits for inspections, maintenance checks and inspections (both scheduled and unscheduled).\"\n" +
            "  } ],\n" +
            "  \"http://www.w3.org/2000/01/rdf-schema#label\" : [ {\n" +
            "    \"@value\" : \"100000219 - 0500 TIME LIMITS/MAINTENANCE CHECKS\"\n" +
            "  } ]\n" +
            "}]";

    private static final String JSON = "[{" +
            "\"id\":\"http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-1-31-26-27-488-10000000-11000000\"," +
            "\"type\":[\"http://onto.fel.cvut.cz/ontologies/eccairs/model/descriptive-factor\"]," +
            "\"description\":\"The aircraft, its systems and components.\"," +
            "\"name\":\"11000000 - Aircraft components and systems\"" +
            "},{" +
            "\"id\":\"http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-1-31-26-27-488-10000000-11000000-100000219\"," +
            "\"type\":[\"http://onto.fel.cvut.cz/ontologies/eccairs/model/descriptive-factor\"]," +
            "\"description\":\"(ATA Code 0500) Manufacturers' recommended time limits for inspections, maintenance checks" +
            " and inspections (both scheduled and unscheduled).\"," +
            "\"name\":\"100000219 - 0500 TIME LIMITS/MAINTENANCE CHECKS\"" +
            "}]";

    private EventTypeJsonLdTransformer transformer = new EventTypeJsonLdTransformer();

    @Test
    public void testTransform() throws Exception {
        final String result = transformer.transform(JSON_LD);
        assertEquals(JSON, result);
    }
}