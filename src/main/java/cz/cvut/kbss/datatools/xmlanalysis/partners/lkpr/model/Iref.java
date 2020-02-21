package cz.cvut.kbss.datatools.xmlanalysis.partners.lkpr.model;

import cz.cvut.kbss.datatools.xmlanalysis.common.refs.annotations.FIDAttribute;
import org.eclipse.persistence.oxm.annotations.XmlPath;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class Iref extends BaseEntity{
    @XmlAttribute
    protected String name;
    @XmlPath("IREF/@type")
    protected String type;
    @FIDAttribute(fieldRef = "modeltype", cls = Model.class)
    @XmlPath("IREF/@tmodeltype")
    protected String tmodeltype;
    @FIDAttribute(fieldRef = "name")
    @XmlPath("IREF/@tmodelname")
    protected String tmodelname;

    @FIDAttribute(value = "1", fieldRef = "cls", cls = Instance.class)
    @XmlPath("IREF/@tclassname")
    protected String tclassname;

    @FIDAttribute(value = "1", fieldRef = "name")
    @XmlPath("IREF/@tobjname")
    protected String tobjname;


    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getTmodeltype() {
        return tmodeltype;
    }

    public String getTmodelname() {
        return tmodelname;
    }

    public String getTclassname() {
        return tclassname;
    }

    public String getTobjname() {
        return tobjname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTmodeltype(String tmodeltype) {
        this.tmodeltype = tmodeltype;
    }

    public void setTmodelname(String tmodelname) {
        this.tmodelname = tmodelname;
    }

    public void setTclassname(String tclassname) {
        this.tclassname = tclassname;
    }

    public void setTobjname(String tobjname) {
        this.tobjname = tobjname;
    }


    @Override
    public String toString() {
        return "Interref{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", tmodeltype='" + tmodeltype + '\'' +
                ", tmodelname='" + tmodelname + '\'' +
                ", tclassname='" + tclassname + '\'' +
                ", tobjname='" + tobjname + '\'' +
                '}';
    }
}
