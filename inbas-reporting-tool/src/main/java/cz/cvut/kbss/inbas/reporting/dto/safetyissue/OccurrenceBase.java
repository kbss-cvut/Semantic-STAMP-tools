package cz.cvut.kbss.inbas.reporting.dto.safetyissue;

import java.net.URI;

public class OccurrenceBase extends SafetyIssueBase {

    private String name;

    private URI severity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URI getSeverity() {
        return severity;
    }

    public void setSeverity(URI severity) {
        this.severity = severity;
    }

    @Override
    public String toString() {
        return "OccurrenceBase{<" + getUri() +
                ">, reportKey=" + getReportKey() +
                ", name='" + name + '\'' +
                "} " + super.toString();
    }
}
