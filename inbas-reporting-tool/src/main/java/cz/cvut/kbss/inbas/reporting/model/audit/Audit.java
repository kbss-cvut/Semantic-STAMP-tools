package cz.cvut.kbss.inbas.reporting.model.audit;

import cz.cvut.kbss.inbas.reporting.model.AbstractEntity;
import cz.cvut.kbss.inbas.reporting.model.Organization;
import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@OWLClass(iri = Vocabulary.s_c_Audit)
public class Audit extends AbstractEntity implements Serializable {

    @ParticipationConstraints(nonEmpty = true)
    @OWLAnnotationProperty(iri = Vocabulary.s_p_label)
    private String name;

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.s_p_has_start_time)
    private Date startDate;

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.s_p_has_end_time)
    private Date endDate;

    @ParticipationConstraints(nonEmpty = true)
    // TODO
    private Organization auditedOrganization;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_location)
    private String location;

    @Types
    private Set<String> types;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Organization getAuditedOrganization() {
        return auditedOrganization;
    }

    public void setAuditedOrganization(Organization auditedOrganization) {
        this.auditedOrganization = auditedOrganization;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "Audit{" +
                "name='" + name + '\'' +
                ", startDate=" + startDate +
                ", auditedOrganization=" + auditedOrganization +
                '}';
    }
}
