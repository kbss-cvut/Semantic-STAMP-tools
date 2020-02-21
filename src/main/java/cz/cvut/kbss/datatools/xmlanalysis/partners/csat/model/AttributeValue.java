package cz.cvut.kbss.datatools.xmlanalysis.partners.csat.model;

import javax.xml.bind.annotation.*;


@XmlAccessorType(value = XmlAccessType.FIELD)
public class AttributeValue extends BaseEntity{

    @XmlID
    @XmlAttribute(name = "Id")
    protected String attributeId;

    @XmlElement(name = "Content")
    protected String attributeValue;

    public String getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }
}
