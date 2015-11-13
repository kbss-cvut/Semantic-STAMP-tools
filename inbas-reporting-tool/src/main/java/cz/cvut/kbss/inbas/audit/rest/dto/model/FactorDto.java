package cz.cvut.kbss.inbas.audit.rest.dto.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.net.URI;
import java.util.Date;
import java.util.Set;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class FactorDto {

    private URI uri;

    private Date startTime;

    private Date endTime;

    private Set<FactorDto> children;

    private Set<FactorDto> causes;

    private Set<FactorDto> mitigatingFactors;

    private EventTypeAssessmentDto assessment;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Set<FactorDto> getChildren() {
        return children;
    }

    public void setChildren(Set<FactorDto> children) {
        this.children = children;
    }

    public Set<FactorDto> getCauses() {
        return causes;
    }

    public void setCauses(Set<FactorDto> causes) {
        this.causes = causes;
    }

    public Set<FactorDto> getMitigatingFactors() {
        return mitigatingFactors;
    }

    public void setMitigatingFactors(Set<FactorDto> mitigatingFactors) {
        this.mitigatingFactors = mitigatingFactors;
    }

    public EventTypeAssessmentDto getAssessment() {
        return assessment;
    }

    public void setAssessment(EventTypeAssessmentDto assessment) {
        this.assessment = assessment;
    }
}
