package cz.cvut.kbss.inbas.reporting.dto.safetyissue;

public class AuditFindingBase extends SafetyIssueBase {

    private String description;

    private Integer level;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "AuditFindingBase{<" + getUri() + ">, reportKey=" + getReportKey() + ", description='" + description +
                '\'' + "}";
    }
}
