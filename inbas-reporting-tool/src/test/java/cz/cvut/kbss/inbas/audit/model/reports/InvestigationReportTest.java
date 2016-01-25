package cz.cvut.kbss.inbas.audit.model.reports;

import cz.cvut.kbss.inbas.audit.environment.util.Generator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InvestigationReportTest {

    @Test
    public void constructorFromPreliminaryUsesPreliminaryRevisionIncrementedByOne() throws Exception {
        final PreliminaryReport pr = Generator.generatePreliminaryReport(Generator.ReportType.WITHOUT_TYPE_ASSESSMENTS);
        final int revision = 117;
        pr.setRevision(revision);

        final InvestigationReport result = new InvestigationReport(pr);
        assertEquals(revision + 1, result.getRevision().intValue());
    }
}