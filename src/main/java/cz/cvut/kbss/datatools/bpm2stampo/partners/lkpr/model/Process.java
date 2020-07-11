package cz.cvut.kbss.datatools.bpm2stampo.partners.lkpr.model;

import cz.cvut.kbss.datatools.bpm2stampo.common.refs.annotations.Relation;
import org.eclipse.persistence.oxm.annotations.XmlPath;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Process extends Instance{

    @XmlPath("INTERREF[@name='Referenced process']")
    protected Iref referencedProcessRef;

    @XmlPath("RECORD[@name='Process responsibility']/ROW")
    protected List<ProcessResponsibility> processResponsibility;

    @XmlPath("INTERREF[@name='Input']")
    protected List<Iref> inputDocumentRefs;

    @XmlPath("INTERREF[@name='Output']")
    protected List<Iref> outputDocumentRefs;

    @XmlPath("INTERREF[@name='Referenced documents']")
    protected List<Iref> referencedDocumentRefs;


    @Relation(instanceRef = "referencedProcessRef")
    protected BusinessProcessModel referencedProcess;

    public Iref getReferencedProcessRef() {
        return referencedProcessRef;
    }

    public void setReferencedProcessRef(Iref referencedProcessRef) {
        this.referencedProcessRef = referencedProcessRef;
    }

    public List<ProcessResponsibility> getProcessResponsibility() {
        return processResponsibility;
    }

    public void setProcessResponsibility(List<ProcessResponsibility> processResponsibility) {
        this.processResponsibility = processResponsibility;
    }

    public List<Iref> getInputDocumentRefs() {
        return inputDocumentRefs;
    }

    public void setInputDocumentRefs(List<Iref> inputDocumentRefs) {
        this.inputDocumentRefs = inputDocumentRefs;
    }

    public List<Iref> getOutputDocumentRefs() {
        return outputDocumentRefs;
    }

    public void setOutputDocumentRefs(List<Iref> outputDocumentRefs) {
        this.outputDocumentRefs = outputDocumentRefs;
    }

    public List<Iref> getReferencedDocumentRefs() {
        return referencedDocumentRefs;
    }

    public void setReferencedDocumentRefs(List<Iref> referencedDocumentRefs) {
        this.referencedDocumentRefs = referencedDocumentRefs;
    }

    public BusinessProcessModel getReferencedProcess() {
        return referencedProcess;
    }

    public void setReferencedProcess(BusinessProcessModel referencedProcess) {
        this.referencedProcess = referencedProcess;
    }
}
