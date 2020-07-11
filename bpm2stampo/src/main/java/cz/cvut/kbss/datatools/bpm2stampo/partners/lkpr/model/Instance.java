package cz.cvut.kbss.datatools.bpm2stampo.partners.lkpr.model;


import org.eclipse.persistence.oxm.annotations.XmlKey;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import java.util.List;

//@XmlRootElement(name = "INSTANCE")
@XmlAccessorType(XmlAccessType.FIELD)
public class Instance extends BaseEntity{
//    @InstanceId
    @XmlID
    @XmlAttribute
    protected String id;

    @XmlKey
    @XmlAttribute(name = "class")
    protected String cls;

    @XmlKey
    @XmlAttribute
    protected String name;

    protected List<Connector> hasNotes;

    public Instance() {
    }

    public Instance(String id, String cls, String name) {
        this.id = id;
        this.cls = cls;
        this.name = name;
    }


    public List<Connector> getHasNotes() {
        return hasNotes;
    }

    public void setHasNotes(List<Connector> hasNotes) {
        this.hasNotes = hasNotes;
    }

    public String getId() { return id;}

    public String getCls() {return cls; }

    public String getName() {return name;}

    public void setId(String id) {
        this.id = id;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Instance{" +
                "id='" + id + '\'' +
                ", cls='" + cls + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
