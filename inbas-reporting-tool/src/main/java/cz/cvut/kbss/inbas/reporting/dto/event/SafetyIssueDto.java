package cz.cvut.kbss.inbas.reporting.dto.event;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import cz.cvut.kbss.inbas.reporting.dto.reportlist.ReportDto;

import java.util.Set;

@JsonIdentityInfo(property = "referenceId", generator = ObjectIdGenerators.PropertyGenerator.class)
public class SafetyIssueDto extends EventDto {

    private String name;

    private Set<ReportDto> basedOn;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ReportDto> getBasedOn() {
        return basedOn;
    }

    public void setBasedOn(Set<ReportDto> basedOn) {
        this.basedOn = basedOn;
    }
}
