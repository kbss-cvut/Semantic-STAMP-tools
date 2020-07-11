package cz.cvut.kbss.datatools.bpm2stampo.partners.lkpr.model;

import org.eclipse.persistence.oxm.annotations.XmlPath;

import javax.xml.bind.annotation.*;

/**
 * Description of a responsibility of a performer or a role in a process in a company map.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ProcessResponsibility extends BaseEntity{

    @XmlID
    @XmlAttribute
    protected String id;

    @XmlPath("ATTRIBUTE/[name='Classification']/text()")
    protected String classification;
    @XmlPath("ATTRIBUTE/[name='Description']/text()")
    protected String description;

    @XmlPath("INTERREF[name='Role/Performer']/IREF'")
    protected Iref roleOrPerformer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Iref getRoleOrPerformer() {
        return roleOrPerformer;
    }

    public void setRoleOrPerformer(Iref roleOrPerformer) {
        this.roleOrPerformer = roleOrPerformer;
    }
}
