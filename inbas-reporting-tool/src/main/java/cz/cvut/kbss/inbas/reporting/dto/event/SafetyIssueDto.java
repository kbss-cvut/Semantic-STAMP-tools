package cz.cvut.kbss.inbas.reporting.dto.event;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import cz.cvut.kbss.inbas.reporting.dto.safetyissue.SafetyIssueBase;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

@JsonIdentityInfo(property = "referenceId", generator = ObjectIdGenerators.PropertyGenerator.class)
public class SafetyIssueDto extends EventDto {

    private String name;

    private Set<SafetyIssueBase> basedOn;

    private URI state;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<SafetyIssueBase> getBasedOn() {
        return basedOn;
    }

    public void setBasedOn(Set<SafetyIssueBase> basedOn) {
        this.basedOn = basedOn;
    }

    public void addBase(SafetyIssueBase base) {
        if (basedOn == null) {
            this.basedOn = new HashSet<>();
        }
        basedOn.add(base);
    }

    public URI getState() {
        return state;
    }

    public void setState(URI state) {
        this.state = state;
    }
}
