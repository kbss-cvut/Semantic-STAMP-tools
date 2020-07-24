package cz.cvut.kbss.datatools.bpm2stampo.converters.bizagi.model;

import org.eclipse.persistence.oxm.annotations.XmlPath;

import javax.xml.bind.annotation.*;


@XmlAccessorType(value = XmlAccessType.FIELD)
public class Lane extends BaseEntity  {
    @XmlID
    @XmlAttribute(name = "Id")
    protected String id;
    @XmlAttribute(name = "Name")
    protected String name;

    @XmlPath("NodeGraphicsInfos/NodeGraphicsInfo/Coordinates/@XCoordinate")
    protected double x;
    @XmlPath("NodeGraphicsInfos/NodeGraphicsInfo/Coordinates/@YCoordinate")
    protected double y;
    @XmlPath("NodeGraphicsInfos/NodeGraphicsInfo/@Width")
    protected double widht;
    @XmlPath("NodeGraphicsInfos/NodeGraphicsInfo/@Height")
    protected double height;


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
}
