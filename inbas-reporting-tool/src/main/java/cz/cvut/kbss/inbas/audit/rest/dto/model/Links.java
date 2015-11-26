package cz.cvut.kbss.inbas.audit.rest.dto.model;

import java.util.HashSet;
import java.util.Set;

public class Links {

    private Set<Link> causes;

    private Set<Link> mitigates;

    public Set<Link> getCauses() {
        return causes;
    }

    public void setCauses(Set<Link> causes) {
        this.causes = causes;
    }

    public void addCause(Link cause) {
        if (causes == null) {
            this.causes = new HashSet<>();
        }
        causes.add(cause);
    }

    public Set<Link> getMitigates() {
        return mitigates;
    }

    public void setMitigates(Set<Link> mitigates) {
        this.mitigates = mitigates;
    }

    public void addMitigates(Link mitigate) {
        if (mitigates == null) {
            this.mitigates = new HashSet<>();
        }
        mitigates.add(mitigate);
    }

    @Override
    public String toString() {
        return "Links{" +
                "causes=" + (causes != null ? causes.size() : 0) +
                ", mitigates=" + (mitigates != null ? mitigates.size() : 0) +
                '}';
    }
}
