package cz.cvut.kbss.datatools.xmlanalysis.experiments.jaxb;

import org.eclipse.persistence.oxm.annotations.XmlJoinNode;
import org.eclipse.persistence.oxm.annotations.XmlJoinNodes;
import org.eclipse.persistence.oxm.annotations.XmlKey;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
//import org.eclipse.persistence.oxm.annotations.XmlI
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class ExperimentJaxbApp2 {


    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Company{

        @XmlElementRef
        protected List<Employee> employees;

        public List<Employee> getEmployees() {
            return employees;
        }

        public void setEmployees(List<Employee> employees) {
            this.employees = employees;
        }

        @Override
        public String toString() {
            return "Company{" +
                    "employees=" + employees +
                    '}';
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Employee {
        @XmlID
        @XmlAttribute
        private Integer id;

        @XmlKey
        @XmlAttribute
        private String name;

        @XmlJoinNodes( {
                @XmlJoinNode(xmlPath = "manager/@id", referencedXmlPath = "@id"),
                @XmlJoinNode(xmlPath = "manager/@name", referencedXmlPath = "@name") })
        public Employee manager;

        @XmlJoinNodes( {
                @XmlJoinNode(xmlPath = "report/@id", referencedXmlPath = "@id"),
                @XmlJoinNode(xmlPath = "report/@name", referencedXmlPath = "@name") })
        public List<Employee> reports = new ArrayList<>();

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Employee getManager() {
            return manager;
        }

        public void setManager(Employee manager) {
            this.manager = manager;
        }

        public List<Employee> getReports() {
            return reports;
        }

        public void setReports(List<Employee> reports) {
            this.reports = reports;
        }

        @Override
        public String toString() {
            return "Employee{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", manager=" + manager +
                    ", reports=" + reports +
                    '}';
        }
    }

    public static String xmlExample1(){
        return "<company>\n" +
                "   <employee id=\"1\" name=\"Jane Doe\">\n" +
                "      <report id=\"2\" name=\"John Smith\"/>\n" +
                "      <report id=\"3\" name=\"Anne Jones\"/>\n" +
                "   </employee>\n" +
                "   <employee id=\"2\" name=\"John Smith\">\n" +
                "      <manager id=\"1\" name=\"Jane Doe\"/>\n" +
                "   </employee>\n" +
                "   <employee id=\"3\" name=\"Anne Jones\">\n" +
                "      <manager id=\"1\" name=\"Jane Doe\"/>\n" +
                "   </employee>\n" +
                "</company>";
    }

    public static void main(String[] args) {
        String xmlString = xmlExample1();

        JAXBContext jaxbContext;
        try
        {
            jaxbContext = JAXBContext.newInstance(Employee.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            Company company = (Company) jaxbUnmarshaller.unmarshal(new StringReader(xmlString));

            System.out.println(company);
//            for(Employee employee : company.getEmployees()) {
//                System.out.println(employee);
//            }
        }
        catch (JAXBException e)
        {
            e.printStackTrace();
        }
    }


}
