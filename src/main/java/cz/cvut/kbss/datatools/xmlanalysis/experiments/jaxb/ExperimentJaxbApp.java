package cz.cvut.kbss.datatools.xmlanalysis.experiments.jaxb;

//import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;

//import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;

import cz.cvut.kbss.datatools.xmlanalysis.experiments.jaxb.model.Employee;
import cz.cvut.kbss.datatools.xmlanalysis.experiments.jaxb.model.Employees;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.lang.reflect.Method;

//import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;

public class ExperimentJaxbApp {


    public static void printVersion(JAXBContext jaxbContext){
        try {
            Method m = jaxbContext.getClass().getDeclaredMethod("getBuildId");
            System.out.println(jaxbContext.getClass().getCanonicalName());
//            JAXBContextImpl
            if(m != null){
                System.out.println(m.invoke(jaxbContext));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void runApp(){
        String xmlString =
                "<employees>" +
                "<employee id='1'>" +
                "    <department>" +
                "        <id>101</id>" +
                "        <name>IT</name>" +
                "    </department>" +
                "    <firstName>Lokesh</firstName>" +
//                "    <id>1</id>" +
                "    <lastName>Gupta</lastName>" +
                "    <manager>" +
                "       <firstName>Steve</firstName>" +
                "       <lastName>Jobs</lastName>" +
                "    </manager>" +
                "    <boss fn='Steve' ln='Jobs'>" +
                "    </boss >" +
                "</employee>" +
                "<employee id='2'>" +
                "    <department>" +
                "        <id>101</id>" +
                "        <name>IT</name>" +
                "    </department>" +
                "    <firstName>Steve</firstName>" +
//                "    <id>1</id>" +
                "    <lastName>Jobs</lastName>" +
                "</employee>" +
                "</employees>";
        JAXBContext jaxbContext;
        try
        {
            jaxbContext = JAXBContext.newInstance(Employees.class);

            printVersion(jaxbContext);

//            Binder<Node> binder = jaxbContext.createBinder();

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();


            jaxbUnmarshaller.setListener(new Unmarshaller.Listener() {
                @Override
                public void beforeUnmarshal(Object target, Object parent) {
                    System.out.println(target.getClass().getCanonicalName());
                }

                @Override
                public void afterUnmarshal(Object target, Object parent) {
                    System.out.println(target.getClass().getCanonicalName());
//                    binder.getXMLNode();
//                    EntityRegistry.
                }
            });

            Employees employees = (Employees) jaxbUnmarshaller.unmarshal(new StringReader(xmlString));

            for(Employee employee : employees.getEmployees()) {
                System.out.println(employee);
            }
        }
        catch (JAXBException e)
        {
            e.printStackTrace();
        }

    }
    
    public static void main(String[] args) {
        runApp();
    }
}
