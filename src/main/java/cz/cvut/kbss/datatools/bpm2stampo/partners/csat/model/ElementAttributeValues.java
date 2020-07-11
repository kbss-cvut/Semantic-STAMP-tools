package cz.cvut.kbss.datatools.bpm2stampo.partners.csat.model;

import org.eclipse.persistence.oxm.annotations.XmlPath;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import java.util.List;


//@XmlRootElement(name = "ElementAttributeValues")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class ElementAttributeValues extends BaseEntity{

    @XmlID
    @XmlAttribute(name = "ElementId")
    protected String elementId;

//    @XmlElementWrapper(name = "Values")
//    @XmlElement(name = "ExtendedAttributeValue")
    @XmlPath("Values/ExtendedAttributeValue")
    protected List<AttributeValue> attributeValueList;

//    @XmlPath("Values/ExtendedAttributeValue[@Id='469c5dc2-d945-4f85-ba98-d0c42869d886']")
//    protected AttributeValue controlLoop;
//
//    @XmlPath("Values/ExtendedAttributeValue[@Id='063c1630-b46a-4fc9-8514-3dbd4b373d98']")
//    protected AttributeValue actuators;
//
//    @XmlPath("Values/ExtendedAttributeValue[@Id='e30b483b-be2b-4eeb-a125-6724bdc81729']")
//    protected AttributeValue sensors;
//
//    @XmlPath("Values/ExtendedAttributeValue[@Id='5ad36dfb-ed0b-4519-b1cc-dce6cbcee08e']")
//    protected AttributeValue deviations;
//
//    public String getElementId() {
//        return elementId;
//    }
//
//    public void setElementId(String elementId) {
//        this.elementId = elementId;
//    }
//
//    public AttributeValue getControlLoop() {
//        return controlLoop;
//    }
//
//    public void setControlLoop(AttributeValue controlLoop) {
//        this.controlLoop = controlLoop;
//    }
//
//    public AttributeValue getActuators() {
//        return actuators;
//    }
//
//    public void setActuators(AttributeValue actuators) {
//        this.actuators = actuators;
//    }
//
//    public AttributeValue getSensors() {
//        return sensors;
//    }
//
//    public void setSensors(AttributeValue sensors) {
//        this.sensors = sensors;
//    }
//
//    public AttributeValue getDeviations() {
//        return deviations;
//    }
//
//    public void setDeviations(AttributeValue deviations) {
//        this.deviations = deviations;
//    }

    public List<AttributeValue> getAttributeValueList() {
        return attributeValueList;
    }

    public void setAttributeValueList(List<AttributeValue> attributeValueList) {
        this.attributeValueList = attributeValueList;
    }
}
