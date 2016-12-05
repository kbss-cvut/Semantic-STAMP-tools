package cz.cvut.kbss.inbas.reporting.dto.safetyissue;

import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import cz.cvut.kbss.inbas.reporting.model.audit.AuditFinding;

import java.util.HashSet;

public class AuditFindingBase extends SafetyIssueBase {

    private String description;

    private String level;

    public AuditFindingBase() {
    }

    public AuditFindingBase(AuditFinding finding) {
        setUri(finding.getUri());
        this.description = finding.getDescription();
        this.level = finding.getLevel();
        setTypes(new HashSet<>(finding.getTypes()));
        getTypes().add(Vocabulary.s_c_audit_finding);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "AuditFindingBase{<" + getUri() + ">, reportKey=" + getReportKey() + ", description='" + description +
                '\'' + "}";
    }
}
