package cz.cvut.kbss.datatools.xmlanalysis.partners.csat.model;

import cz.cvut.kbss.datatools.xmlanalysis.common.refs.annotations.FIDAttribute;
import org.eclipse.persistence.oxm.annotations.XmlPath;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

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
