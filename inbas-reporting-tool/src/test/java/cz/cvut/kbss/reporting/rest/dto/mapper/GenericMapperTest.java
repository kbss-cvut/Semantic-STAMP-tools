package cz.cvut.kbss.reporting.rest.dto.mapper;

import cz.cvut.kbss.reporting.dto.OccurrenceReportDto;
import cz.cvut.kbss.reporting.environment.util.Environment;
import cz.cvut.kbss.reporting.model.Agent;
import cz.cvut.kbss.reporting.model.OccurrenceReport;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.*;

public class GenericMapperTest {

    private GenericMapper mapper = new GenericMapper(new DtoMapperImpl());

    @Test
    public void mapMapsOccurrenceReportDtoToOccurrenceReport() throws Exception {
        final OccurrenceReportDto dto = Environment
                .loadData("data/occurrenceReportWithFactorGraph.json", OccurrenceReportDto.class);
        final Object result = mapper.map(dto);
        assertNotNull(result);
        assertTrue(result instanceof OccurrenceReport);
    }

    @Test
    public void mapReturnsTheSameInstanceIfItCannotBeMapped() {
        final Agent agent = new Agent();
        agent.setUri(URI.create("http://agent007"));
        final Object result = mapper.map(agent);
        assertSame(agent, result);
    }

    @Test
    public void mapReturnsNullForNullArgument() {
        assertNull(mapper.map(null));
    }
}