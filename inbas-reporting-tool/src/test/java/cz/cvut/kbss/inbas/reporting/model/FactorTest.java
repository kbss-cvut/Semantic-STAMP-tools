package cz.cvut.kbss.inbas.reporting.model;

import cz.cvut.kbss.inbas.reporting.model.reports.Factor;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertNull;

public class FactorTest {

    @Test
    public void factorCopyConstructorSkipsEventTypeAssessmentForFactorsWithoutIt() {
        final Factor root = new Factor();
        root.setStartTime(new Date(System.currentTimeMillis() - 1000));
        root.setEndTime(new Date());

        final Factor result = new Factor(root);
        assertNull(result.getAssessment());
    }
}
