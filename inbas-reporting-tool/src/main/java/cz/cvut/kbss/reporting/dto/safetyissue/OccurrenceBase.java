package cz.cvut.kbss.reporting.dto.safetyissue;

import cz.cvut.kbss.reporting.model.Occurrence;
import cz.cvut.kbss.reporting.model.Vocabulary;

import java.net.URI;
import java.util.HashSet;
import java.util.Objects;

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

    public OccurrenceBase() {
    }

    public OccurrenceBase(Occurrence occurrence) {
        Objects.requireNonNull(occurrence);
        this.setUri(occurrence.getUri());
        this.setName(occurrence.getName());
        this.setTypes(new HashSet<>(occurrence.getTypes()));
        this.getTypes().add(Vocabulary.s_c_Occurrence);
    }

    @Override
    public String toString() {
        return "OccurrenceBase{<" + getUri() +
                ">, reportKey=" + getReportKey() +
                ", name='" + name + '\'' +
                "} " + super.toString();
    }
}
