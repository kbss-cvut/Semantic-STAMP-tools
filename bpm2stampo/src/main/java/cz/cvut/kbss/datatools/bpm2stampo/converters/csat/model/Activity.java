    package cz.cvut.kbss.datatools.bpm2stampo.converters.csat.model;

import cz.cvut.kbss.datatools.bpm2stampo.common.refs.annotations.*;
import org.eclipse.persistence.oxm.annotations.XmlPath;

import javax.xml.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@XmlRootElement(name = "Activity")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class Activity extends BaseEntity{
    // This is uses an old limited functionality of the @Relation. Change to use the @FarRelation
    @FIDAttribute(value = "1", fieldRef = "elementId", cls = ElementAttributeValues.class)
    @XmlID
    @XmlAttribute(name = "Id")
    protected String id;

    @XmlAttribute(name = "Name")
    protected String name;

    @RelationType(relationType = RelationTypes.manyToOne)
    @XmlElementWrapper(name = "Performers")
    @XmlElement(name = "Performer")
    protected List<PerformerRef> performerRefs;

    @XmlPath("Event/StartEvent")
    protected Event startEvent;

    @XmlPath("Event/IntermediateEvent")
    protected Event intermediateEvent;

    @XmlPath("Event/EndEvent")
    protected Event endEvent;

    @XmlPath("Route")
    protected Route route;

    @XmlPath("NodeGraphicsInfos/NodeGraphicsInfo/Coordinates/@XCoordinate")
    protected double x;
    @XmlPath("NodeGraphicsInfos/NodeGraphicsInfo/Coordinates/@YCoordinate")
    protected double y;
    @XmlPath("NodeGraphicsInfos/NodeGraphicsInfo/@Width")
    protected double widht;
    @XmlPath("NodeGraphicsInfos/NodeGraphicsInfo/@Height")
    protected double height;



    @RelationType(relationType = RelationTypes.manyToOne)
    @FIDAttribute(value = "2", fieldRef = "id", cls = Activity.class)
    @XmlPath("Event/IntermediateEvent/@Target")
    protected String activityId;

    // Fields for Tasks
    @XmlPath("Implementation/Task")
    protected ImplementationTask implementationTask;

    @XmlPath("Implementation/SubFlow")
    protected ImplementationSubFLow implementationSubFlow;

    // Fields for Tasks
    @XmlPath("Implementation/Task/*/@name")
    protected String taskType;



    // Relational fields

    @Relation(instanceRef = "performerRefs")
    protected List<Participant> performers;

    @Relation(value = "1")
    protected ElementAttributeValues elementAttributeValues;

    @FarRelation(value = "2")
    protected List<Activity> events;

    @Relation(value = "1", instanceRef = "implementationSubFlow")
    protected WorkflowProcess subProcess;


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

    public ElementAttributeValues getElementAttributeValues() {
        return elementAttributeValues;
    }

    public void setElementAttributeValues(ElementAttributeValues elementAttributeValues) {
        this.elementAttributeValues = elementAttributeValues;
    }


    public ImplementationTask getImplementationTask() {
        return implementationTask;
    }

    public void setImplementationTask(ImplementationTask implementationTask) {
        this.implementationTask = implementationTask;
    }

    public ImplementationSubFLow getImplementationSubFlow() {
        return implementationSubFlow;
    }

    public void setImplementationSubFlow(ImplementationSubFLow implementationSubFlow) {
        this.implementationSubFlow = implementationSubFlow;
    }

    public boolean isImplementation(){
        return implementationTask != null || implementationSubFlow != null;
    }

    public boolean isImplementationSubFlow(){
        return implementationSubFlow != null;
    }

    public boolean isImplementationTask(){
        return implementationTask != null;
    }

    public boolean isStartEvent(){
        return startEvent != null;
    }
    public boolean isEndEvent(){
        return endEvent != null;
    }
    public boolean isIntermediateEvent(){
        return intermediateEvent != null;
    }

    public Event getStartEvent() {
        return startEvent;
    }

    public void setStartEvent(Event startEvent) {
        this.startEvent = startEvent;
    }

    public Event getIntermediateEvent() {
        return intermediateEvent;
    }

    public void setIntermediateEvent(Event intermediateEvent) {
        this.intermediateEvent = intermediateEvent;
    }

    public Event getEndEvent() {
        return endEvent;
    }

    public void setEndEvent(Event endEvent) {
        this.endEvent = endEvent;
    }

    protected boolean isValueSet(String val){
        return val != null && !val.isEmpty();
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public WorkflowProcess getSubProcess() {
        return subProcess;
    }

    public void setSubProcess(WorkflowProcess subProcess) {
        this.subProcess = subProcess;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidht() {
        return widht;
    }

    public void setWidht(double widht) {
        this.widht = widht;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public List<Activity> getEvents() {
        return events;
    }

    public void setEvents(List<Activity> events) {
        this.events = events;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
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
