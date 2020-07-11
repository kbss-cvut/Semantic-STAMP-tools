package cz.cvut.kbss.datatools.xmlanalysis.experiments.jaxb;

import cz.cvut.kbss.datatools.xmlanalysis.common.Utils;
import cz.cvut.kbss.datatools.xmlanalysis.common.refs.IDRuntime;
import cz.cvut.kbss.datatools.xmlanalysis.partners.lkpr.model.ADOXML;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Combines JAXB unmarshaller and the ID framework
 */
public class JaxbExtractor {

    public static void runApp(){
        JAXBContext jaxbContext;
        try
        {

            String path = ".\\lkpr-process-models\\example-models-01\\model-001.xml";
//            String path = ".\\lkpr-process-models\\example-models-01\\adonis-connector-example.xml";

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(Utils.getFile(path));

            jaxbContext = JAXBContext.newInstance(ADOXML.class);

//            Binder<Node> binder = jaxbContext.createBinder();

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Set<Object> unmarshaled = new HashSet<>();
            jaxbUnmarshaller.setListener(new Unmarshaller.Listener() {
                @Override
                public void beforeUnmarshal(Object target, Object parent) {}

                @Override
                public void afterUnmarshal(Object target, Object parent) {
                    unmarshaled.add(target);
//                    if(parent != null)
//                    unmarshaled.add(parent);
                }
            });


            System.out.println(doc.getDocumentElement().getTagName());
            ADOXML adoxml = (ADOXML) jaxbUnmarshaller.unmarshal(doc.getDocumentElement());
            new IDRuntime().injectReferences(unmarshaled);
//            for(Model model : adoxml.getModels()) {
//                System.out.println(model);
//            }
        }
        catch (JAXBException e)
        {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void main(String[] args) {
        runApp();
    }
}
