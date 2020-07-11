package cz.cvut.kbss.datatools.bpm2stampo.partners.csat.model;

import org.eclipse.persistence.oxm.annotations.XmlPath;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "DiagramAttributeValues")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class DiagramAttributeValues extends BaseEntity{

    @XmlPath("ElementAttributeValues")
    protected List<ElementAttributeValues> extendedAttributeValueList;

    public List<ElementAttributeValues> getExtendedAttributeValueList() {
        return extendedAttributeValueList;
    }

    public void setExtendedAttributeValueList(List<ElementAttributeValues> extendedAttributeValueList) {
        this.extendedAttributeValueList = extendedAttributeValueList;
    }
}
