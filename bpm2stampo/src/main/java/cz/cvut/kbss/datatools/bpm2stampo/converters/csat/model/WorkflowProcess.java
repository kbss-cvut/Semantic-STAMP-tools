package cz.cvut.kbss.datatools.bpm2stampo.converters.csat.model;

import org.eclipse.persistence.oxm.annotations.XmlPath;

import javax.xml.bind.annotation.*;
import java.util.List;


@XmlRootElement(name = "WorkflowProcess")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class WorkflowProcess extends BaseEntity {

    @XmlID
    @XmlAttribute(name = "Id")
    protected String id;

    @XmlAttribute(name = "Name")
    protected String name;

    @XmlPath("Activities/Activity")
    protected List<Activity> activities;

    @XmlElementWrapper(name = "Transitions")
    @XmlElement(name = "Transition")
    protected List<Transition> transitions;

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

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public void setTransitions(List<Transition> transitions) {
        this.transitions = transitions;
    }

    @Override
    public String toString() {
        return "WorkflowProcess{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
