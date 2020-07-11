package cz.cvut.kbss.datatools.bpm2stampo.partners.lkpr.model;

import cz.cvut.kbss.datatools.bpm2stampo.common.refs.annotations.Relation;
import cz.cvut.kbss.datatools.bpm2stampo.common.refs.annotations.RelationType;
import cz.cvut.kbss.datatools.bpm2stampo.common.refs.annotations.RelationTypes;
import org.eclipse.persistence.oxm.annotations.XmlPath;

import java.util.List;

public class Activity extends Instance {

    @RelationType(relationType = RelationTypes.manyToOne)
    @XmlPath("RECORD[@name='Risk/control allocation']/ROW/INTERREF[@name='Risk']")
    protected List<Iref> riskRefs;

    @Relation(value = "1", instanceRef = "riskRefs")
    protected List<Instance> risks;

    @XmlPath("INTERREF[@name='Responsible role']")
    protected Iref responsibleRoleRef;

    @Relation(value = "1", instanceRef = "responsibleRoleRef")
    protected Instance responsibleRole;


    public List<Iref> getRiskRefs() {
        return riskRefs;
    }

    public void setRiskRefs(List<Iref> riskRefs) {
        this.riskRefs = riskRefs;
    }

    public List<Instance> getRisks() {
        return risks;
    }

    public void setRisks(List<Instance> risks) {
        this.risks = risks;
    }

    public Iref getResponsibleRoleRef() {
        return responsibleRoleRef;
    }

    public void setResponsibleRoleRef(Iref responsibleRoleRef) {
        this.responsibleRoleRef = responsibleRoleRef;
    }

    public Instance getResponsibleRole() {
        return responsibleRole;
    }

    public void setResponsibleRole(Instance responsibleRole) {
        this.responsibleRole = responsibleRole;
    }
}
