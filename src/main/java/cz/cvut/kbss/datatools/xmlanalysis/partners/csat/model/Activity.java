package cz.cvut.kbss.datatools.xmlanalysis.partners.csat.model;

import cz.cvut.kbss.datatools.xmlanalysis.common.refs.annotations.ManyFK;
import cz.cvut.kbss.datatools.xmlanalysis.common.refs.annotations.Relation;

import javax.xml.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@XmlRootElement(name = "Activity")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class Activity implements BaseEntity{
    @XmlID
    @XmlAttribute(name = "Id")
    protected String id;
    @XmlAttribute(name = "Name")
    protected String name;

    @ManyFK
    @XmlElementWrapper(name = "Performers")
    @XmlElement(name = "Performer")
    protected List<PerformerRef> performerRefs;


    @Relation(instanceRef = "performerRefs")
    protected List<Participant> performers;


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

    public List<PerformerRef> getPerformerRefs() {
        return performerRefs;
    }

    public void setPerformerRefs(List<PerformerRef> performerRefs) {
        this.performerRefs = performerRefs;
    }

    public List<Participant> getPerformers() {
        return performers;
    }

    public void setPerformers(List<Participant> performers) {
        this.performers = performers;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", performers = (" + Optional.ofNullable(performers)
                    .map(ps -> ps.stream().map(i -> String.format("'%s'",i)).collect(Collectors.joining( ", ")))
                    .orElse("-") + ")" +
                '}';
    }
}
