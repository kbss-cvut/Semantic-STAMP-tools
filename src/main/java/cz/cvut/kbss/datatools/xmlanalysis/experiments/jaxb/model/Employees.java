package cz.cvut.kbss.datatools.xmlanalysis.experiments.jaxb.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Employees {

    protected List<Employee> employees;


    public List<Employee> getEmployees() {
        return employees;
    }

    @XmlElement(name = "employee")
    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
