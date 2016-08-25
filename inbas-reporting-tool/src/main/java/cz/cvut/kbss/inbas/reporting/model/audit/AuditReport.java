package cz.cvut.kbss.inbas.reporting.model.audit;

import cz.cvut.kbss.inbas.reporting.dto.reportlist.AuditReportDto;
import cz.cvut.kbss.inbas.reporting.dto.reportlist.ReportDto;
import cz.cvut.kbss.inbas.reporting.model.AbstractReport;
import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.FetchType;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;

import java.io.Serializable;

@OWLClass(iri = Vocabulary.s_c_audit_report)
public class AuditReport extends AbstractReport implements Serializable {

    @ParticipationConstraints(nonEmpty = true)
    @OWLObjectProperty(iri = Vocabulary.s_p_documents, fetch = FetchType.EAGER)
    private Audit audit;

    public AuditReport() {
    }

    public AuditReport(AuditReport other) {
        super(other);
        assert other.audit != null;
        this.audit = new Audit(other.audit);
    }

    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
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
