package cz.cvut.kbss.datatools.bpm2stampo.converters.bizagi.model;

import cz.cvut.kbss.datatools.bpm2stampo.common.refs.annotations.FIDAttribute;
import org.eclipse.persistence.oxm.annotations.XmlPath;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(value = XmlAccessType.FIELD)
public class PerformerRef extends BaseEntity {
    @FIDAttribute(cls = Participant.class)
    @XmlPath("text()")
    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
