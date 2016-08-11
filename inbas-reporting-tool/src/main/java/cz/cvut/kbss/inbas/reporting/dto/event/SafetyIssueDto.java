package cz.cvut.kbss.inbas.reporting.dto.event;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import cz.cvut.kbss.inbas.reporting.dto.OccurrenceReportDto;

import java.util.Set;

@JsonIdentityInfo(property = "referenceId", generator = ObjectIdGenerators.PropertyGenerator.class)
public class SafetyIssueDto extends EventDto {

    private String name;

    private Set<OccurrenceReportDto> basedOn;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<OccurrenceReportDto> getBasedOn() {
        return basedOn;
    }

    public void setBasedOn(Set<OccurrenceReportDto> basedOn) {
        this.basedOn = basedOn;
    }
}
