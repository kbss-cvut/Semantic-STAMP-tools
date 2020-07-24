package cz.cvut.kbss.datatools.bpm2stampo.converters.adonis.model;

import cz.cvut.kbss.datatools.bpm2stampo.common.refs.annotations.Relation;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "CONNECTOR")
@XmlAccessorType(XmlAccessType.FIELD)
public class Connector extends BaseEntity{

    @XmlID
    @XmlAttribute
    protected String id;

    @XmlAttribute(name = "class")
    protected String cls;

    @XmlElement(name = "FROM")
    protected InstanceRef fromInstanceRef;

    @XmlElement(name = "TO")
    protected InstanceRef toInstanceRef;


    // references
    @Relation(instanceRef = "fromInstanceRef")
    protected Instance from;

    @Relation(instanceRef = "toInstanceRef")
    protected Instance to;


    public String getId() { return id; }

    public String getCls() {return cls; }

    public InstanceRef getFromInstanceRef() { return fromInstanceRef;}

    public InstanceRef getToInstanceRef() { return toInstanceRef; }

    public void setId(String id) {
        this.id = id;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public void setFromInstanceRef(InstanceRef fromInstance) {
        this.fromInstanceRef = fromInstance;
    }

    public void setToInstanceRef(InstanceRef toInstance) {
        this.toInstanceRef = toInstance;
    }

    public Instance getFrom() {
        return from;
    }

    public void setFrom(Instance from) {
        this.from = from;
    }

    public Instance getTo() {
        return to;
    }

    public void setTo(Instance to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "Connector{" +
                "id='" + id + '\'' +
                ", cls='" + cls + '\'' +
                ", fromInstanceRef=" + fromInstanceRef +
                ", toInstanceRef=" + toInstanceRef +
                ", from =" + from +
                ", to =" + to +
                '}';
    }
}
