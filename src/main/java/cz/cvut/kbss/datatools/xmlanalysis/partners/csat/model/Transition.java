package cz.cvut.kbss.datatools.xmlanalysis.partners.csat.model;

import cz.cvut.kbss.datatools.xmlanalysis.common.refs.annotations.FIDAttribute;
import cz.cvut.kbss.datatools.xmlanalysis.common.refs.annotations.Relation;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "Transition")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class Transition extends BaseEntity {
    @XmlID
    @XmlAttribute(name = "Id")
    protected String id;

    @FIDAttribute(cls=Activity.class, fieldRef = "id")
    @XmlAttribute(name = "From")
    protected String fromId;

    @FIDAttribute(cls=Activity.class, value = "1", fieldRef = "id")
    @XmlAttribute(name = "To")
    protected String toId;

    @Relation
    protected Activity from;
    @Relation( value= "1")
    protected Activity to;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public Activity getFrom() {
        return from;
    }

    public void setFrom(Activity from) {
        this.from = from;
    }

    public Activity getTo() {
        return to;
    }

    public void setTo(Activity to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "Transition{" +
                "id='" + id + '\'' +
                ", from='" + fromId + '\'' +
                ", to='" + toId + '\'' +
                '}';
    }
}
