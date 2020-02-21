package cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf;

import cz.cvut.kbss.datatools.xmlanalysis.common.Utils;
import cz.cvut.kbss.jopa.model.IRI;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;

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
        String id = JAXBUtils.extractIdFromJAXBAnnotations(o);
        if(id == null)
            id = getUniqueTimeId();;
        return generateIRI(o, id);
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
