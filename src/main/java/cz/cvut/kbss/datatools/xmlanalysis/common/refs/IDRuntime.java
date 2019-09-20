package cz.cvut.kbss.datatools.xmlanalysis.common.refs;

import cz.cvut.kbss.datatools.xmlanalysis.common.refs.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class IDRuntime {

    private static final Logger LOG = LoggerFactory.getLogger(IDRuntime.class);

    protected IdRelationalSchema schema;
    protected Map<String, Object> registry;

    public void injectReferences(Collection<Object> objects){
        buildSchema(objects);

        objects.forEach(o -> {
            ClassMetadata classMetadata = schema.getClass2MetadataMap().get(o.getClass());
            for (Key key : classMetadata.getKeys()) {

            }
        });
        // Input list of objects
        // - for each object do
        // - calculate its keys and put it into K2O registry
        buildRegistry(objects);
        injectImpl(objects);

    }

    /**
     * - for each object do
     * - For each R do
     * 1) Find instance that contains the FK, this could be 1) the current instance, 2) the value of a field
     * 2) calculate the FK w.r.t. to the instance containing the FK
     * 3) look up the an object using the FK value in the K2O registry
     * 4) inject using reflection into the relation
     * @param objects
     */
    protected void injectImpl(Collection<Object> objects){

        for (Object o : objects) {
            ClassMetadata classMetadata = schema.getClass2MetadataMap().get(o.getClass());
            for(RelationField r : classMetadata.getRelations()){
                if(r.isManyRelation()){
                    injectManyValues(o, r);
                }else{
                    injectSingleValue(o, r);
                }
            }
        }
    }

    protected void injectSingleValue(Object o, RelationField r){
        Object fkContainerInstance = o;
        if(r.getContainerField() != null){
            fkContainerInstance = ReflectionUtils.getValue(o, r.getContainerField());
        }

        String kv = calculateId(fkContainerInstance, r.getKeyMapping().getForeignKey());
        Object toBeInjected = registry.get(kv);
        ReflectionUtils.setValue(o, r.getRelationField(), toBeInjected);
    }

    protected void injectManyValues(Object o, RelationField r){
//        Object fkContainerInstances = o;
        Collection sourceCollection;
        Collection targetCollection = ReflectionUtils.getCollectionInstanceAndSetField(o, r.getRelationField()); // TODO get the collection instance of the getRelationField, or create a new collection based on the type

        if(r.getContainerField() != null){
            // collection of instances containing a keymapping
            sourceCollection = (Collection) ReflectionUtils.getValue(o, r.getContainerField());// collection of instances containing keys
            if(sourceCollection == null)return;

            for(Object fkContainerInstance : sourceCollection) {
                String kv = calculateId(fkContainerInstance, r.getKeyMapping().getForeignKey());
                Object toBeInjected = registry.get(Objects.toString(kv));
                targetCollection.add(toBeInjected);
            }
        }else{
            // collection of single attribute keys.
            sourceCollection = (Collection) ReflectionUtils.getValue(o, r.getKeyMapping().getForeignKey().getFields().get(0).getValue());
            if(sourceCollection == null)return;
            for(Object k : sourceCollection){
                Object toBeInjected = registry.get(Objects.toString(k));
                targetCollection.add(toBeInjected);
            }
        }
    }


    protected void buildSchema(Collection<Object> objects){
        Set<Class> classes = objects.stream().map(o -> o.getClass()).collect(Collectors.toSet());
        ExtractIDMetadata schemaExtractor = new ExtractIDMetadata();
        schema = schemaExtractor.buildSchema(classes);
    }

    protected void buildRegistry(Collection<Object> objects){
        registry = new HashMap<>();
        for (Object o : objects) {
            ClassMetadata classMetadata = schema.getClass2MetadataMap().get(o.getClass());
            for(Key k : classMetadata.getKeys()) {
                String kv = calculateId(o, k);
                if(kv != null) {
                    Object old = registry.put(kv, o);
                    if(old != null && old != o ){
                        LOG.error("object with duplicate ID!!! {}", kv);
                    }
                }
            }
        }
    }

    protected String calculateId(Object obj, Key key){
        // Fixing BUG: What if instances from different classes have the same keys.
        // change to generate the value - ClassName[field1.Name=field1.Value;fieldN.Name=fieldN.Value]
        // TODO: change to use the key id. This requires to fix a bug with the naming of keys

        return String.format("%s[%s]",
                key.getRefedClass().getCanonicalName(),
                key.getFields().stream().map(f ->
                        String.format(
                                "%s=%s",
                                f.getKey(),
                                Objects.toString(ReflectionUtils.getValue(obj, f.getValue()))
                        )
                ).collect(Collectors.joining(";"))
        );
    }


    public IdRelationalSchema getSchema() {
        return schema;
    }

    public void setSchema(IdRelationalSchema schema) {
        this.schema = schema;
    }

}
