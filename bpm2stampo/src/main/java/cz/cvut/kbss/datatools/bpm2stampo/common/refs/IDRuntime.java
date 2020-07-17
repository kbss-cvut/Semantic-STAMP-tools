package cz.cvut.kbss.datatools.bpm2stampo.common.refs;

import cz.cvut.kbss.datatools.bpm2stampo.common.refs.model.ClassMetadata;
import cz.cvut.kbss.datatools.bpm2stampo.common.refs.model.IdRelationalSchema;
import cz.cvut.kbss.datatools.bpm2stampo.common.refs.model.Key;
import cz.cvut.kbss.datatools.bpm2stampo.common.refs.model.RelationField;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class IDRuntime {

    private static final Logger LOG = LoggerFactory.getLogger(IDRuntime.class);

    protected IdRelationalSchema schema;
    protected Map<String, Object> toOneRegistry;
//    protected Map<String, List<Object>> toManyRegistry;

    public void injectReferences(Collection<Object> objects){
        buildSchema(objects);

//        objects.forEach(o -> {
//            ClassMetadata classMetadata = schema.getClass2MetadataMap().get(o.getClass());
//            for (Key key : classMetadata.getToOneKeys()) {
//
//            }
//        });
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
                if(r.isManyRelationField()){
                    injectManyValues(o, r);
                }else{
                    injectSingleValue(o, r);
                }
            }

            for(RelationField r : classMetadata.getFarRelations()){
                farInjectSingleValueInField(o, r);
            }
        }
    }

    protected void injectSingleValue(Object o, RelationField r){
        Pair p = calculateSingleInjectionArguments(o, r);
        if(p != null)
            ReflectionUtils.setValue(p.getLeft(), r.getRelationField(), p.getRight());
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
                if(kv == null)
                    continue;
                Object toBeInjected = toOneRegistry.get(Objects.toString(kv));
                targetCollection.add(toBeInjected);
            }
        }else{
            // collection of single attribute keys.
            sourceCollection = (Collection) ReflectionUtils.getValue(o, r.getKeyMapping().getForeignKey().getFields().get(0).getValue());
            if(sourceCollection == null)return;
            for(Object keyAttributeValue : sourceCollection){
                String kv = calculateIdFromValue(r.getKeyMapping().getForeignKey(), keyAttributeValue);
                if(kv == null)
                    continue;
                Object toBeInjected = toOneRegistry.get(kv);
                targetCollection.add(toBeInjected);
            }
        }
    }

    protected void farInjectSingleValueInField(Object o, RelationField r){
        Pair p = calculateSingleInjectionArguments(o, r);
        if(p == null)return;
        // o == p.left
        Field rf = r.getRelationField();

        if(r.isManyRelationField()) {
            Collection targetCollection = ReflectionUtils.getCollectionInstanceAndSetField(p.getRight(), r.getRelationField());
            targetCollection.add(p.getLeft());
        }else if(rf.getType().isArray()) {
            throw new RuntimeException("Injection in arrays is not supported!!!");
        }else{// Arrays not supported!!!
            ReflectionUtils.setValue(p.getRight(), r.getRelationField(), p.getLeft());
        }
    }

    protected void farInjectSingleValueInCollectionField(Object o, RelationField r){
        Collection sourceCollection;
        Collection targetCollection = ReflectionUtils.getCollectionInstanceAndSetField(o, r.getRelationField()); // TODO get the collection instance of the getRelationField, or create a new collection based on the type

        if(r.getContainerField() != null){
            // collection of instances containing a keymapping
            sourceCollection = (Collection) ReflectionUtils.getValue(o, r.getContainerField());// collection of instances containing keys
            if(sourceCollection == null)return;

            for(Object fkContainerInstance : sourceCollection) {
                String kv = calculateId(fkContainerInstance, r.getKeyMapping().getForeignKey());
                if(kv == null)
                    continue;
                Object toBeInjected = toOneRegistry.get(kv);
                targetCollection.add(toBeInjected);
            }
        }else{
            // collection of single attribute keys.
            sourceCollection = (Collection) ReflectionUtils.getValue(o, r.getKeyMapping().getForeignKey().getFields().get(0).getValue());
            if(sourceCollection == null)return;
            for(Object keyAttributeValue : sourceCollection){
                String kv = calculateIdFromValue(r.getKeyMapping().getForeignKey(), keyAttributeValue);
                if(kv == null)
                    continue;
                Object toBeInjected = toOneRegistry.get(kv);
                targetCollection.add(toBeInjected);
            }
        }
    }

    protected Pair<Object, Object> calculateSingleInjectionArguments(Object side1, RelationField r){
        Object fkContainerInstance = side1;
        if(r.getContainerField() != null){
            fkContainerInstance = ReflectionUtils.getValue(side1, r.getContainerField());
        }
        if(fkContainerInstance == null)
            return null;

        String kv = calculateId(fkContainerInstance, r.getKeyMapping().getForeignKey());
        if(kv == null)
            return null;
        Object side2 = toOneRegistry.get(kv);
        if(side2 == null)
            return null;
        return Pair.of(side1, side2);
    }


    protected void buildSchema(Collection<Object> objects){
        Set<Class> classes = objects.stream().map(o -> o.getClass()).collect(Collectors.toSet());
        ExtractIDMetadata schemaExtractor = new ExtractIDMetadata();
        schema = schemaExtractor.buildSchema(classes);
    }

    protected void buildRegistry(Collection<Object> objects){
        // build ToOneRegistry
        toOneRegistry = new HashMap<>();

        for (Object o : objects) {
            Set<ClassMetadata> classMetadataEntries = getClassMetadataEntries(o.getClass());
            for(ClassMetadata classMetadata: classMetadataEntries) {
                for (Key k : classMetadata.getToOneKeys()) {
                    String kv = calculateId(o, k);
                    if (kv != null) {
                        Object old = toOneRegistry.put(kv, o);
                        if (old != null && old != o) {
                            LOG.debug("error - object with duplicate ID!!! {}", kv);
                        }
                    }
                }
            }
        }
    }

    protected Set<ClassMetadata> getClassMetadataEntries(Class cls){
        return schema.getClass2MetadataMap().entrySet().stream()
                .filter(e -> e.getKey().isAssignableFrom(cls))
                .map(e -> e.getValue())
                .collect(Collectors.toSet());
    }

    protected String calculateIdFromValue(Key key, Object val){
        return calculateId(key, Arrays.asList(Pair.of(key.getFields().get(0).getKey(), val)));
    }

    protected String calculateId(Object obj, Key key){
        // Fixing BUG: What if instances from different classes have the same keys.
        // change to generate the value - ClassName[field1.Name=field1.Value;fieldN.Name=fieldN.Value]
        // TODO: change to use the key id. This requires to fix a bug with the naming of keys
        return calculateId(
                key,
                key.getFields().stream().map(p ->
                        Pair.of(
                                p.getKey(),
                                ReflectionUtils.getValue(obj, p.getValue())
                        )
                ).collect(Collectors.toList())
        );
    }

    protected String calculateId(Key key, Collection<Pair<String, Object>> keyValuePairs){
        if(keyValuePairs.stream().anyMatch(p -> p.getValue() == null))
            return null;

        String keyValue = keyValuePairs.stream()
                .map(p -> String.format("%s=%s", p.getKey(), Objects.toString(p.getValue())))
                .collect(Collectors.joining(";"));
        return String.format("%s[%s]",
                key.getRefedClass().getCanonicalName(),
                keyValue
        );
    }


    public IdRelationalSchema getSchema() {
        return schema;
    }

    public void setSchema(IdRelationalSchema schema) {
        this.schema = schema;
    }

}
