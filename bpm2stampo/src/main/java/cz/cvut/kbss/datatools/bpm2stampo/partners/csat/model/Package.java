package cz.cvut.kbss.datatools.bpm2stampo.partners.csat.model;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "Package")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class Package extends BaseEntity{

    @XmlID
    @XmlAttribute(name = "Id")
    protected String id;
    @XmlAttribute(name = "Name")
    protected String name;

    @XmlElementWrapper(name = "Participants")
    @XmlElement(name = "Participant")
    protected List<Participant> participants;

    @XmlElementWrapper(name = "Pools")
    @XmlElement(name = "Pool")
    protected List<Pool> pools;

    @XmlElementWrapper(name = "WorkflowProcesses")
    @XmlElement(name = "WorkflowProcess")
    protected List<WorkflowProcess> processes;

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public List<WorkflowProcess> getProcesses() {
        return processes;
    }

    public void setProcesses(List<WorkflowProcess> processes) {
        this.processes = processes;
    }

    public List<Pool> getPools() {
        return pools;
    }

    public void setPools(List<Pool> pools) {
        this.pools = pools;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Package{" +
                "participants=" + participants +
                ", processes=" + processes +
                '}';
    }
}
