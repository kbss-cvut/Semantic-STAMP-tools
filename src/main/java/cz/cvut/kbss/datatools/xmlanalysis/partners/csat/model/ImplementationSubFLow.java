package cz.cvut.kbss.datatools.xmlanalysis.partners.csat.model;

import cz.cvut.kbss.datatools.xmlanalysis.common.refs.annotations.FIDAttribute;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SubFlow")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class ImplementationSubFLow {

    @FIDAttribute(value = "1", fieldRef = "id", cls=WorkflowProcess.class)
    @XmlAttribute(name = "Id")
    protected String referencedProcessId;

    public String getReferencedProcessId() {
        return referencedProcessId;
    }

    public void setReferencedProcessId(String referencedProcessId) {
        this.referencedProcessId = referencedProcessId;
    }
}
