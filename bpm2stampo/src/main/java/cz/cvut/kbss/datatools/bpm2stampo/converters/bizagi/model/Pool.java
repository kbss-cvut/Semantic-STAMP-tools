package cz.cvut.kbss.datatools.bpm2stampo.converters.bizagi.model;

import cz.cvut.kbss.datatools.bpm2stampo.common.refs.annotations.FIDAttribute;
import cz.cvut.kbss.datatools.bpm2stampo.common.refs.annotations.Relation;
import org.eclipse.persistence.oxm.annotations.XmlPath;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "Package")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class Pool extends BaseEntity {
    @XmlID
    @XmlAttribute(name = "Id")
    protected String id;
    @XmlAttribute(name = "Name")
    protected String name;

    @FIDAttribute(fieldRef =  "id", cls = WorkflowProcess.class)
    @XmlAttribute(name = "Process")
    protected String processId;


    @XmlAttribute(name = "BoundaryVisible")
    protected boolean boundaryVisible;

    @XmlPath("NodeGraphicsInfos/NodeGraphicsInfo/Coordinates/@XCoordinate")
    protected double x;
    @XmlPath("NodeGraphicsInfos/NodeGraphicsInfo/Coordinates/@YCoordinate")
    protected double y;
    @XmlPath("NodeGraphicsInfos/NodeGraphicsInfo/@Width")
    protected double widht;
    @XmlPath("NodeGraphicsInfos/NodeGraphicsInfo/@Height")
    protected double height;

    @Relation
    protected WorkflowProcess process;

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

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public boolean isBoundaryVisible() {
        return boundaryVisible;
    }

    public void setBoundaryVisible(boolean boundaryVisible) {
        this.boundaryVisible = boundaryVisible;
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

    public WorkflowProcess getProcess() {
        return process;
    }

    public void setProcess(WorkflowProcess process) {
        this.process = process;
    }
}
