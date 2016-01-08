package cz.cvut.kbss.inbas.audit.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.net.URI;
import java.util.Date;
import java.util.Set;

// Identity generator is used to identify factors in the causes/mitigates relationships
// Note that every reference has to be created before it is used in an causes/mitigatingFactors array in JSON
// See reportWithFactorsWithCauses.json in test resources
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "referenceId")
public class FactorDto {

    private URI uri;

    private Date startTime;

    private Date endTime;

    private Set<FactorDto> children;

    private EventTypeAssessmentDto assessment;

    private Integer referenceId;

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

    public EventTypeAssessmentDto getAssessment() {
        return assessment;
    }

    public void setAssessment(EventTypeAssessmentDto assessment) {
        this.assessment = assessment;
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }
}
