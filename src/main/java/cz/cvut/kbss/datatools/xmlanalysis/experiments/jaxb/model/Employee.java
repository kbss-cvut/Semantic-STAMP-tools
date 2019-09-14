package cz.cvut.kbss.datatools.xmlanalysis.experiments.jaxb.model;

import org.eclipse.persistence.oxm.annotations.XmlJoinNode;
import org.eclipse.persistence.oxm.annotations.XmlJoinNodes;
import org.eclipse.persistence.oxm.annotations.XmlKey;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "employee")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Employee {

//    @XmlAttribute(name = "id")
    protected String id;
    protected String firstName;
    protected String lastName;


    protected Employee manager;

    protected Employee boss;

    protected Department department;


    public Employee() {
    }

    public Employee(String id, String firstName, String lastName, Department department) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
    }

    public String getId() {
        return id;
    }


    @XmlID
    @XmlAttribute(name = "id")
    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    @XmlKey
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @XmlKey
    public void setLastName(String lname) {
        this.lastName = lname;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }



    public Employee getManager() {
        return manager;
    }

    public Employee getBoss() {
        return boss;
    }

    @XmlJoinNodes({
            @XmlJoinNode(xmlPath = "manager/firstName/text()", referencedXmlPath = "firstName/text()"),
            @XmlJoinNode(xmlPath = "manager/lastName/text()", referencedXmlPath = "lastName/text()")
    })
    public void setManager(Employee manager) {
        this.manager = manager;
    }


    @XmlJoinNodes({
            @XmlJoinNode(xmlPath = "boss/@fn", referencedXmlPath = "firstName/text()"),
            @XmlJoinNode(xmlPath = "boss/@ln", referencedXmlPath = "lastName/text()")
    })
    public void setBoss(Employee boss) {
        this.boss = boss;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", department=" + department +
                ", manager=" + manager +
                ", boss=" + boss +
                '}';
    }
}
