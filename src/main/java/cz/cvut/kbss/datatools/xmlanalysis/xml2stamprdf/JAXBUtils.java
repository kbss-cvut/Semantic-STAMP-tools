package cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf;

import cz.cvut.kbss.datatools.xmlanalysis.common.refs.ReflectionUtils;
import cz.cvut.kbss.datatools.xmlanalysis.partners.csat.model.Activity;
import cz.cvut.kbss.datatools.xmlanalysis.partners.csat.model.Package;
import cz.cvut.kbss.datatools.xmlanalysis.partners.csat.model.WorkflowProcess;
import cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.jaxbmodel.BaseXMLEntity;
import cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.jaxbmodel.IBaseXMLEntity;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.eclipse.persistence.oxm.annotations.XmlKey;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlID;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class JAXBUtils {

    public static String extractIdFromJAXBAnnotations(Object o){
        String extractedId = extractIdFromAnnotatedFileds(o, XmlID.class);
        if(extractedId == null)
            extractedId = extractIdFromAnnotatedFileds(o, XmlKey.class);
        return extractedId;
    }

    protected static String extractIdFromAnnotatedFileds(Object o, Class<? extends Annotation> c){
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(o.getClass(), c);
        return computeId(o, fields);
    }

    public static String computeId(Object o, List<Field> idFields){
        idFields.sort(Comparator.comparing(f -> f.getName())); // sort the fields to construct an id string from possibly composite key
        String id = null;
        if(idFields != null) {
            StringBuilder idb = new StringBuilder();
            for (Field f : idFields) {
                Object ido = Optional.ofNullable(ReflectionUtils.getValue(o, f)).orElse(null);
                if(ido != null ){
                    String idc = Objects.toString(ido).trim();
                    if(!idc.isEmpty())
                        idb.append(idc).append('-');
                }
            }
            if(idb.length() > 0){
                idb.deleteCharAt(idb.length() - 1);
                String idTmp = idb.toString().trim();
                if(!idTmp.isEmpty()){
                    id = idTmp;
                }
            }
        }
        return id;
    }

    public static List<Field> extractJAXBIdFields(Class cls){
        List<Field> extractedId = FieldUtils.getFieldsListWithAnnotation(cls, XmlID.class);
        if(extractedId.isEmpty())
            extractedId = FieldUtils.getFieldsListWithAnnotation(cls, XmlKey.class);
        return extractedId;
    }

    public static <T> UnmarshledResult<T> unmarshalXMLResolveRelations(String filePath, InputStream inputStream, Class<T> cls) {//} throws IOException {
        return unmarshalXMLResolveRelations(filePath, inputStream, cls, null);
    }

    public static <T> UnmarshledResult<T> unmarshalXMLResolveRelations(String filePath, InputStream inputStream, Class<T> cls, String schemaPath){// throws IOException {

        JAXBContext jaxbContext;
        try
        {
            Document doc = parseDocumentUnsafe(filePath, inputStream, schemaPath);

            jaxbContext = JAXBContext.newInstance(cls);
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
//            new IDRuntime().injectReferences(unmarshaled);
            injectParents(unmarshaled);
            // DEBUG
//            int bad = 0;
//            int good = 0;
//            for(Object o: unmarshaled){
////                BaseXMLEntity be = (BaseXMLEntity)o;
//
//                if(o instanceof WorkflowProcess) {
//                    WorkflowProcess be = (WorkflowProcess)o;
//                    if(be.getParent() == null || !(be.getParent() instanceof Package)){
//
//                        bad++;
//                    }else {
//                        good ++;
//                    }
//                }
//            }
//
//            System.out.println(String.format("bad = %d\ngood = %d", bad, good));
            //END DEBUG
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

    protected static void injectParents(Collection<Object> unmarshaledObjects){
        BiConsumer setParents = (from, to) -> {
            // DEBUG
//            if(to instanceof WorkflowProcess){
//                if(from instanceof Activity){
//                    System.out.println("Asdf");
//                }
//            }
            // END DEBUG

            if(from instanceof IBaseXMLEntity && to instanceof IBaseXMLEntity){
                ((IBaseXMLEntity)to).setParent((IBaseXMLEntity)from);
            }
        };

        ReflectionUtils.traverseGraph(unmarshaledObjects, setParents);
    }


    public static Document parseDocumentUnsafe(String filePath, InputStream inputStream, String schemaPath) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        setSchema(schemaPath, dbf, db);

        return db.parse(inputStream);
    }

    public static Document parseDocument(String filePath, InputStream inputStream, String schemaPath){
        try {
            return parseDocumentUnsafe(filePath, inputStream, schemaPath);
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
