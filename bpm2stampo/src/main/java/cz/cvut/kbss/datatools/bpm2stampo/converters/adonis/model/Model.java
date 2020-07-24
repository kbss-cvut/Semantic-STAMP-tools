package cz.cvut.kbss.datatools.bpm2stampo.converters.adonis.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;


@XmlAccessorType(XmlAccessType.FIELD)
public class Model extends BaseEntity{

    @XmlID
    @XmlAttribute
    protected String id;

    @XmlAttribute
    protected String name;

    @XmlAttribute
    protected String modeltype;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getModeltype() {
        return modeltype;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setModeltype(String modeltype) {
        this.modeltype = modeltype;
    }

    @Override
    public String toString() {
        return "Model{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", modeltype='" + modeltype + '\'' +
                '}';
    }
}
