package cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf;

import cz.cvut.kbss.datatools.xmlanalysis.common.Utils;
import cz.cvut.kbss.datatools.xmlanalysis.common.refs.ReflectionUtils;
import cz.cvut.kbss.jopa.model.IRI;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.eclipse.persistence.oxm.annotations.XmlKey;

import javax.xml.bind.annotation.XmlID;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BasicIRIGenerator implements IRIGenerator {

    protected String rootIRI;

    public String getRootIRI() {
        return rootIRI;
    }

    public void setRootIRI(String rootIRI) {
        this.rootIRI = rootIRI;
    }


    public static String getOWLClassName(Object o){
        OWLClass owlClass = o.getClass().getAnnotation(OWLClass.class);
        String iriString = owlClass.iri();
        IRI iri = IRI.create(iriString);
        String[] components = iriString.split("[#/]");
        return Utils.urlEncode(components[components.length - 1]);
    }

    public static String getClassName(Object o){
        return o.getClass().getSimpleName();
    }

    @Override
    public String generateIRI(Object o) {
        String id = getUniqueTimeId();
        String extractedId = extractId(o, XmlID.class);
        if(extractedId == null)
            extractedId = extractId(o, XmlKey.class);
        if(extractedId != null)
            id = extractedId;
        return generateIRI(o, id);
    }

    protected String extractId(Object o, Class c){
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(o.getClass(), c);
        fields.sort(Comparator.comparing(f -> f.getName())); // sort the fields to construct an id string from possibly composite key
        String id = null;
        if(fields != null) {
            StringBuilder idb = new StringBuilder();
            for (Field f : fields) {
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

    protected static long last_t = 0;
    public synchronized static String getUniqueTimeId(){
            for(int i = 0; i < 4; i++) {
                long t = System.currentTimeMillis();
                if(t != last_t) {
                    last_t = t;
                    break;
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        return last_t + "";
    }

    public String generateIRI(Object o, String id){
        return rootIRI + getClassName(o) + "-class-" + id;
    }
}
