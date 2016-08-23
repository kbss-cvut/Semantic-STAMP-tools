package cz.cvut.kbss.inbas.reporting.model.audit;

import cz.cvut.kbss.inbas.reporting.dto.reportlist.AuditReportDto;
import cz.cvut.kbss.inbas.reporting.dto.reportlist.ReportDto;
import cz.cvut.kbss.inbas.reporting.model.AbstractReport;
import cz.cvut.kbss.inbas.reporting.model.CorrectiveMeasureRequest;
import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.util.Set;

@OWLClass(iri = Vocabulary.s_c_audit_report)
public class AuditReport extends AbstractReport implements Serializable {

    @ParticipationConstraints(nonEmpty = true)
    @OWLObjectProperty(iri = Vocabulary.s_p_documents, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Audit audit;

    // TODO
    private Set<AuditFinding> findings;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_corrective_measure, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<CorrectiveMeasureRequest> correctiveMeasures;

    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }

    public Set<AuditFinding> getFindings() {
        return findings;
    }

    public void setFindings(Set<AuditFinding> findings) {
        this.findings = findings;
    }

    public Set<CorrectiveMeasureRequest> getCorrectiveMeasures() {
        return correctiveMeasures;
    }

    public void setCorrectiveMeasures(
            Set<CorrectiveMeasureRequest> correctiveMeasures) {
        this.correctiveMeasures = correctiveMeasures;
    }

    @Override
    public ReportDto toReportDto() {
        final AuditReportDto dto = new AuditReportDto();
        copyAttributes(dto);
        dto.setIdentification(audit.getName());
        dto.setSummary(summary);
        dto.getTypes().add(AuditReport.class.getDeclaredAnnotation(OWLClass.class).iri());
        return dto;
    }
}
