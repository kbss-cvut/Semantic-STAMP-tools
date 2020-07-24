package cz.cvut.kbss.datatools.bpm2stampo.converters.adonis.model;

import org.eclipse.persistence.oxm.annotations.XmlPath;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class BusinessProcessModel extends Model{
    // Mappings and fields
//    @XmlPath("INSTANCE[contains('" +
//            "Activity, Swimlane (vertical), Swimlane (horizontal), " +
//            "Process Start, End, Decision, " +
//            "Merging, Parallelity, Subprocess, Trigger', " +
//            "@class)]")
    @XmlPath("INSTANCE")
    protected List<Instance> otherActivities;

    @XmlPath("INSTANCE[@class='Activity']")
    protected List<Activity> activities;

    @XmlPath("CONNECTOR[@class='Subsequent']")
    protected List<Connector> subsequentConnectors;

    @XmlPath("CONNECTOR[@class='Is inside']")
    protected List<Connector> eventPartOfConnectors;


    // Getters and Setters


    public List<Instance> getOtherActivities() {
        return otherActivities;
    }

    public void setOtherActivities(List<Instance> otherActivities) {
        this.otherActivities = otherActivities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public List<Connector> getSubsequentConnectors() {
        return subsequentConnectors;
    }

    public void setSubsequentConnectors(List<Connector> subsequentConnectors) {
        this.subsequentConnectors = subsequentConnectors;
    }

    public List<Connector> getEventPartOfConnectors() {
        return eventPartOfConnectors;
    }

    public void setEventPartOfConnectors(List<Connector> eventPartOfConnectors) {
        this.eventPartOfConnectors = eventPartOfConnectors;
    }


}
