package cz.cvut.kbss.datatools.xmlanalysis.partners.lkpr.model;

import org.eclipse.persistence.oxm.annotations.XmlPath;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class CompanyMapModel extends Model {
    @XmlPath("INSTANCE[@class='Process']")
    protected List<Process> processes;

    @XmlPath("INSTANCE[@class='Swimlane (Horizontal)']")
    protected List<Instance> swimlanesH;

    @XmlPath("INSTANCE[@class='Swimlane (Vertical)']")
    protected List<Instance> swimlanesV;

    @XmlPath("INSTANCE[@class='actor']")
    protected List<Instance> actors;

    @XmlPath("INSTANCE[@class='External partner']")
    protected List<Instance> externalPartners;

    @XmlPath("INSTANCE[@class='Aggregation']")
    protected List<Instance> aggregations;

    @XmlPath("CONNECTOR[@class='Has process']")
    protected List<Connector> hasChildProcess;

    @XmlPath("CONNECTOR[@class='Is inside']")
    protected List<Connector> isInside;

    public List<Process> getProcesses() {
        return processes;
    }

    public void setProcesses(List<Process> processes) {
        this.processes = processes;
    }

    public List<Connector> getHasChildProcess() {
        return hasChildProcess;
    }

    public void setHasChildProcess(List<Connector> hasChildProcess) {
        this.hasChildProcess = hasChildProcess;
    }

    public List<Instance> getSwimlanesH() {
        return swimlanesH;
    }

    public void setSwimlanesH(List<Instance> swimlanesH) {
        this.swimlanesH = swimlanesH;
    }

    public List<Instance> getSwimlanesV() {
        return swimlanesV;
    }

    public void setSwimlanesV(List<Instance> swimlanesV) {
        this.swimlanesV = swimlanesV;
    }

    public List<Instance> getActors() {
        return actors;
    }

    public void setActors(List<Instance> actors) {
        this.actors = actors;
    }

    public List<Instance> getExternalPartners() {
        return externalPartners;
    }

    public void setExternalPartners(List<Instance> externalPartners) {
        this.externalPartners = externalPartners;
    }

    public List<Instance> getAggregations() {
        return aggregations;
    }

    public void setAggregations(List<Instance> aggregations) {
        this.aggregations = aggregations;
    }

    public List<Connector> getIsInside() {
        return isInside;
    }

    public void setIsInside(List<Connector> isInside) {
        this.isInside = isInside;
    }
}
