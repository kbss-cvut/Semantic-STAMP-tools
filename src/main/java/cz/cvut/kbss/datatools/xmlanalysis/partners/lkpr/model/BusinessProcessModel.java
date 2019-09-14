package cz.cvut.kbss.datatools.xmlanalysis.partners.lkpr.model;

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
    protected List<Instance> activities;

    @XmlPath("CONNECTOR[@class='Subsequent']")
    protected List<Connector> subsequentConnectors;

    @XmlPath("CONNECTOR[@class='Is inside']")
    protected List<Connector> eventPartOfConnectors;


    // Getters and Setters
    public List<Instance> getActivities() {
        return activities;
    }

    public void setActivities(List<Instance> activities) {
        this.activities = activities;
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
