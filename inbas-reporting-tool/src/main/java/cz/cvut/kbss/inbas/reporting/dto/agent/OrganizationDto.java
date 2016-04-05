package cz.cvut.kbss.inbas.reporting.dto.agent;

import java.util.Set;

public class OrganizationDto extends AgentDto {

    private String name;

    private Set<String> types;

    public OrganizationDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }
}
