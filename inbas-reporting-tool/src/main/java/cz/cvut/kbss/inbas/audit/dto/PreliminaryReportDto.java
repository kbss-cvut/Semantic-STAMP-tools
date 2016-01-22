package cz.cvut.kbss.inbas.audit.dto;

import java.util.Set;

public class PreliminaryReportDto extends AbstractReportDto {

    private Set<EventTypeAssessmentDto> typeAssessments;

    public Set<EventTypeAssessmentDto> getTypeAssessments() {
        return typeAssessments;
    }

    public void setTypeAssessments(Set<EventTypeAssessmentDto> typeAssessments) {
        this.typeAssessments = typeAssessments;
    }

    @Override
    public String toString() {
        return "PreliminaryReportDto{" +
                "uri=" + getUri() +
                ", key='" + getKey() + '\'' +
                ", summary='" + getSummary() + '\'' +
                ", occurrence=" + getOccurrence() +
                '}';
    }
}
