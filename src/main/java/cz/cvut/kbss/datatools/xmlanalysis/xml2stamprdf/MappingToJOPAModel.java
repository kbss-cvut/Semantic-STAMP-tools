package cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf;

import cz.cvut.kbss.jopa.model.EntityManager;

import java.util.Collections;
import java.util.List;

/**
 * unfinished idea
 */
@Deprecated
public class MappingToJOPAModel {
    protected BasicIRIGenerator iriGenerator;

    protected EntityManager entityMAnager;

    public MappingToJOPAModel(String rootIRI) {
        BasicIRIGenerator ig = new BasicIRIGenerator();
        ig.setRootIRI(rootIRI);
        iriGenerator = ig;
    }

    public BasicIRIGenerator getIriGenerator() {
        return iriGenerator;
    }

    public void setIriGenerator(BasicIRIGenerator iriGenerator) {
        this.iriGenerator = iriGenerator;
    }

    public EntityManager getEntityMAnager() {
        return entityMAnager;
    }

    public void setEntityMAnager(EntityManager entityMAnager) {
        this.entityMAnager = entityMAnager;
    }


    public void serialize(Object obj){
//        entityMAnager.p
//        List<Object> objects = traversObjects();
//        for( : objects){
//
//        }
    }

    public List<Object> traversObjects(){
        return Collections.emptyList();
    }
}
