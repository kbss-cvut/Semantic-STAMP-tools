package cz.cvut.kbss.datatools.xmlanalysis.partners.csat.model;

import cz.cvut.kbss.datatools.xmlanalysis.common.refs.annotations.FIDAttribute;
import org.eclipse.persistence.oxm.annotations.XmlPath;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "ExtendedAttribute")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class ExtendedAttribute extends BaseEntity {

    @XmlID
    @XmlAttribute(name = "Id")
    protected String id;

    @XmlPath("Name/text()")
    protected String name;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
