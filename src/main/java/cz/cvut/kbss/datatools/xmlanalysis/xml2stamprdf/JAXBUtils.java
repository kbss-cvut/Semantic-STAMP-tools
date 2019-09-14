package cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf;

import cz.cvut.kbss.datatools.xmlanalysis.common.refs.IDRuntime;
import cz.cvut.kbss.datatools.xmlanalysis.partners.lkpr.model.ADOXML;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

public class JAXBUtils {
    public static <T> UnmarshledResult<T> unmarshalXMLResolveRelations(String filePath, InputStream inputStream, Class<T> cls) throws IOException {
        return unmarshalXMLResolveRelations(filePath, inputStream, cls, null);
    }

    public static <T> UnmarshledResult<T> unmarshalXMLResolveRelations(String filePath, InputStream inputStream, Class<T> cls, String schemaPath) throws IOException {

        JAXBContext jaxbContext;
        try
        {

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            setSchema(schemaPath, dbf, db);

            Document doc = db.parse(inputStream);

            jaxbContext = JAXBContext.newInstance(ADOXML.class);
//            XMLEntityManager
//            Binder<Node> binder = jaxbContext.createBinder();

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Set<Object> unmarshaled = new HashSet<>();
            jaxbUnmarshaller.setListener(new Unmarshaller.Listener() {
                @Override
                public void beforeUnmarshal(Object target, Object parent) {}

                @Override
                public void afterUnmarshal(Object target, Object parent) {
                    unmarshaled.add(target);
                }
            });


            T objT = (T) jaxbUnmarshaller.unmarshal(doc.getDocumentElement());
            new IDRuntime().injectReferences(unmarshaled);

            return new UnmarshledResult<>(objT, unmarshaled);
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
        return null;
    }

    public static void setSchema(String schemaPath, DocumentBuilderFactory dbf, DocumentBuilder db){
        if(schemaPath != null){
            Schema schema = loadSchema(schemaPath);
            dbf.setSchema(schema);
        }else{
            db.setEntityResolver(
                new EntityResolver() {
                    @Override
                    public InputSource resolveEntity(String publicId, String systemId)
                            throws SAXException, IOException {
                        if (systemId != null) {
                            return new InputSource(new StringReader(""));
                        } else {
                            return null;
                        }
                    }
                }
            );
            dbf.setValidating(false);
        }
    }

    public static Schema loadSchema(String schemaFileName) {
        Schema schema = null;
        try {
            //// 1. Lookup a factory for the W3C XML Schema language
            String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
            SchemaFactory factory = SchemaFactory.newInstance(language);

            /*
             * 2. Compile the schema. Here the schema is loaded from a java.io.File, but
             * you could use a java.net.URL or a javax.xml.transform.Source instead.
             */
            schema = factory.newSchema(new File(schemaFileName));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return schema;
    }

    public static class UnmarshledResult<T>{
        protected T root;
        protected Set<Object> objects;

        public UnmarshledResult(T root, Set<Object> objects) {
            this.root = root;
            this.objects = objects;
        }

        public T getRoot() {
            return root;
        }

        public void setRoot(T root) {
            this.root = root;
        }

        public Set<Object> getObjects() {
            return objects;
        }

        public void setObjects(Set<Object> objects) {
            this.objects = objects;
        }
    }
}
