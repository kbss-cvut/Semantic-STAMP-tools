package cz.cvut.kbss.inbas.reporting.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cz.cvut.kbss.inbas.reporting.dto.OccurrenceReportDto;
import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.model_new.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.rest.dto.EventFactorsSerializationTest;
import cz.cvut.kbss.inbas.reporting.rest.dto.mapper.DtoMapper;
import cz.cvut.kbss.inbas.reporting.rest.dto.mapper.DtoMapperImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class OccurrenceReportDtoSerializationTest {

    private ObjectMapper mapper;
    private DtoMapper dtoMapper;

    @Before
    public void setUp() {
        this.mapper = new ObjectMapper();
        this.dtoMapper = new DtoMapperImpl();
    }

    @Test
    public void serializationUsesReferenceToOccurrenceInFactorGraph() throws Exception {
        final OccurrenceReport report = Generator.generateOccurrenceReport(true);
        report.setOccurrence(EventFactorsSerializationTest.generateOccurrenceWithSubEvents());
        final OccurrenceReportDto dto = dtoMapper.occurrenceReportToOccurrenceReportDto(report);
        final String output = mapper.writeValueAsString(dto);
        final JsonNode root = mapper.readTree(output);
        final ObjectNode occurrence = (ObjectNode) root.get("occurrence");
        final String occurrenceRefId = occurrence.get("referenceId").textValue();
        final ObjectNode factorGraph = (ObjectNode) root.get("factorGraph");
        final ArrayNode nodes = (ArrayNode) factorGraph.get("nodes");
        boolean found = false;
        for (int i = 0; i < nodes.size(); i++) {
            JsonNode item = nodes.get(i);
            if (item.asText().equals(occurrenceRefId)) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }
}
