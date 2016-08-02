package cz.cvut.kbss.inbas.reporting.model.safetyissue;

import cz.cvut.kbss.inbas.reporting.dto.reportlist.ReportDto;
import cz.cvut.kbss.inbas.reporting.model.AbstractReport;
import cz.cvut.kbss.inbas.reporting.model.LogicalDocument;
import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.util.Set;

@OWLClass(iri = cz.cvut.kbss.inbas.reporting.model.Vocabulary.s_c_safety_issue_report)
public class SafetyIssueReport extends AbstractReport implements LogicalDocument, Serializable {

    @ParticipationConstraints(nonEmpty = true)
    @OWLObjectProperty(iri = Vocabulary.s_p_documents, fetch = FetchType.EAGER)
    private SafetyIssue safetyIssue;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_corrective_measure, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<CorrectiveMeasure> correctiveMeasures;

    public SafetyIssue getSafetyIssue() {
        return safetyIssue;
    }

    public void setSafetyIssue(SafetyIssue safetyIssue) {
        this.safetyIssue = safetyIssue;
    }

    public Set<CorrectiveMeasure> getCorrectiveMeasures() {
        return correctiveMeasures;
    }

    public void setCorrectiveMeasures(
            Set<CorrectiveMeasure> correctiveMeasures) {
        this.correctiveMeasures = correctiveMeasures;
    }

    @Override
    public ReportDto toReportDto() {
        return null;
    }
}
