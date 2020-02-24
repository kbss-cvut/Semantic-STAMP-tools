package cz.cvut.kbss.datatools.xmlanalysis.partners.csat.model;

import cz.cvut.kbss.datatools.xmlanalysis.common.refs.annotations.FIDAttribute;
import cz.cvut.kbss.datatools.xmlanalysis.common.refs.annotations.Relation;

import javax.xml.bind.annotation.*;


@XmlAccessorType(value = XmlAccessType.FIELD)
public class AttributeValue extends BaseEntity{

    @FIDAttribute(cls = ExtendedAttribute.class, fieldRef = "id")
    @XmlID
    @XmlAttribute(name = "Id")
    protected String attributeId;

    @XmlElement(name = "Content")
    protected String attributeValue;

    @Relation
    protected ExtendedAttribute extendedAttribute;

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

    public ExtendedAttribute getExtendedAttribute() {
        return extendedAttribute;
    }

    public void setExtendedAttribute(ExtendedAttribute extendedAttribute) {
        this.extendedAttribute = extendedAttribute;
    }
}
