package cz.cvut.kbss.datatools.xmlanalysis.common.refs;


import cz.cvut.kbss.datatools.xmlanalysis.common.refs.annotations.FIDAttribute;
import cz.cvut.kbss.datatools.xmlanalysis.common.refs.annotations.ManyFK;
import cz.cvut.kbss.datatools.xmlanalysis.common.refs.annotations.Relation;
import cz.cvut.kbss.datatools.xmlanalysis.common.refs.experiment.C;
import cz.cvut.kbss.datatools.xmlanalysis.common.refs.model.*;
import cz.cvut.kbss.datatools.xmlanalysis.partners.lkpr.model.Instance;
import cz.cvut.kbss.datatools.xmlanalysis.partners.lkpr.model.InstanceRef;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ExtractIDMetadata {
    private static final Logger LOG = LoggerFactory.getLogger(ExtractIDMetadata.class);

    protected IdRelationalSchema schema;
    protected Set<Class> processedClasses;
    ////////////////////////////////////////////////////////////////////
    // Build schema ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////

    protected IdRelationalSchema buildSchema(Collection<Class> classes){
        schema = new IdRelationalSchema();
        processedClasses = new HashSet<>();
        classes.stream().forEach(this::buildClassKeyMappingMetadata);
        classes.stream().forEach(this::buildClassRelationMetadata);
        return schema;
    }




    ////////////////////////////////////////////////////////////////////
    // Extract relations ///////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////

    public void buildClassRelationMetadata(Class cls){
        ClassMetadata clsMetadata = getClassMetadata(cls);
        clsMetadata.setRelations(extractRelationalFields(cls));
    }

    protected List<RelationField> extractRelationalFields(Class cls){
        return FieldUtils.getFieldsListWithAnnotation(cls, Relation.class)
                .stream()
                .map(f -> {
                    Relation r = f.getAnnotation(Relation.class);
                    return createRelationField(cls, r.value(), r.instanceRef(), f);
                })
                .collect(Collectors.toList());

//        return null; // TODO
    }

    protected RelationField createRelationField(Class cls, String keyName, String keyContainerField, Field relationField){
        RelationField relation = new RelationField();
        relation.setRelationField(relationField);
        Class fkContainer = null;


        if(Constants.NO_INSTANCE_REFERENCE.equals(keyContainerField)){
            fkContainer = cls;
            ClassMetadata clsMetadata = schema.getClass2MetadataMap().get(fkContainer);
            KeyMapping keyMapping = clsMetadata.getKeyMapping(keyName);
            relation.setManyRelation(keyMapping.isManyFK());
        }else{
            Field containerField = FieldUtils.getField(cls, keyContainerField, true);
            relation.setContainerField(containerField);
            relation.setManyRelation(containerField.getAnnotation(ManyFK.class) != null);
            if(containerField.getAnnotation(ManyFK.class) != null){
//                fkContainer = containerField.getType();
                if(Collection.class.isAssignableFrom(containerField.getType())) {
                    fkContainer = ReflectionUtils.getGenericParameterClasses(containerField).get(0);
                }else{
                    throw new RuntimeException(String.format("Cannot use ManyFK on non Collection field '%s' in class '%s'.", containerField.getName(), cls.getCanonicalName()));
                }
//                ((ParameterizedType)f.getGenericType()).getActualTypeArguments()
            }else{
                fkContainer = containerField.getType();
            }
        }

        boolean isCollectionRelation = Collection.class.isAssignableFrom(relationField.getType());
        if(relation.isManyRelation() && !isCollectionRelation){
            throw new RuntimeException(String.format("A single fields relation refers to a manyFK field, relation field '%s' in class '%s' .", relationField.getName(), cls.getCanonicalName()));
        }
        if(!relation.isManyRelation() && isCollectionRelation){
            throw new RuntimeException(String.format("A collection relation field refers to a one key mapping, see field '%s' in class '%s'.", relationField.getName(), cls.getCanonicalName()));
        }

        ClassMetadata clsMetadata = schema.getClass2MetadataMap().get(fkContainer);
        KeyMapping keyMapping = clsMetadata.getKeyMapping(keyName);
        relation.setKeyMapping(keyMapping);
        return relation;
    }

    ////////////////////////////////////////////////////////////////////
    // Extract KeyMappings /////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////

    protected void buildClassKeyMappingMetadata(Class cls){
        if(processedClasses.contains(cls)){
            return;
        }

        ClassMetadata classMetadata = getClassMetadata(cls);

        List<KeyMapping> keyMappings = extractForeignKeys(cls);
        classMetadata.setKeyMappings(new ArrayList<>(keyMappings));
        // Associate Keys with their classes
        for(KeyMapping m : keyMappings){
            Key k = m.getKey();
            ClassMetadata clsMetadata = getClassMetadata(k.getCls());
            clsMetadata.getKeys().add(k);
        }
        processedClasses.add(cls);
        Set<Class> additionalClassesToProcess = new HashSet<>();
        for(Field f : FieldUtils.getAllFieldsList(cls)){
            if(Iterable.class.isAssignableFrom(f.getType())){
                Optional.ofNullable(ReflectionUtils.getGenericParameterClasses(f))
                        .ifPresent(additionalClassesToProcess::addAll);
            }
        }

        for(Class c : additionalClassesToProcess){
            buildClassKeyMappingMetadata(c);
        }
    }

    protected ClassMetadata getClassMetadata(Class cls){
        ClassMetadata clsMetadata = schema.getClass2MetadataMap().get(cls);
        if(clsMetadata == null){
            clsMetadata = new ClassMetadata();
            clsMetadata.setKeys(new HashSet<>());
            schema.getClass2MetadataMap().put(cls, clsMetadata);
        }
        return clsMetadata;
    }

    protected List<KeyMapping> extractForeignKeys(Class cls){
        List<KeyMapping> ret = new ArrayList<>();
        List<Field> allKeyAttributeFields = FieldUtils.getFieldsListWithAnnotation(cls, FIDAttribute.class);
        Predicate<Field> isManyFK = f -> f.getAnnotation(ManyFK.class) != null;

        List<KeyMapping> oneFKs = allKeyAttributeFields.stream()
                .filter(f -> !isManyFK.test(f))
                .collect(// group fields by key ID
                        Collectors.groupingBy(f -> f.getAnnotation(FIDAttribute.class).value())
                ).entrySet().stream()
                .map(e -> createKeyMapping(cls, e.getKey(), e.getValue(), false))
                .collect(Collectors.toList());
        ret.addAll(oneFKs);

        List<KeyMapping> manyFKs = allKeyAttributeFields.stream()
                .filter(f -> isManyFK.test(f))
                .collect(// group fields by key ID
                        Collectors.groupingBy(f -> f.getAnnotation(FIDAttribute.class).value())
                ).entrySet().stream()
                .map(e -> createKeyMapping(cls, e.getKey(), e.getValue(), true))
                .collect(Collectors.toList());
        ret.addAll(manyFKs);
        return ret;
    }



    protected KeyMapping createKeyMapping(Class cls, String keyName, List<Field> fields, boolean isManyFK){

        // check consistency
        if (isManyFK && fields.size() > 1) {
            throw new RuntimeException(String.format(
                    "could not construct a manyFK mapping in class '%s', there are more" +
                            " then one attributes associated with one attrubute per keyName = '%s'",
                    cls.getCanonicalName(), keyName
            ));
        }

        // extract and prepare keys to construct a KeyMapping
        Class refedClass = extractReferencedClass(cls, keyName, fields);

        // create foreign Key
        Key fkey = new Key();
        fkey.setKeyName(keyName);
        fkey.setManyFK(isManyFK);
        fkey.setCls(cls);
        fkey.setRefedClass(refedClass);
        fkey.setFields(fields.stream().map(this::createFKAttribute).collect(Collectors.toList()));

        // create key
        Key key = new Key();
        key.setKeyName(keyName);
        key.setCls(refedClass);// this is not a foreign key
        key.setRefedClass(refedClass);
        key.setFields(
                fkey.getFields().stream()
                        .map(p -> createKeyAttribute(refedClass, p))
                        .collect(Collectors.toList())
        );


        KeyMapping keyMapping = new KeyMapping(fkey, key, isManyFK);

        return keyMapping;

    }

    protected Class extractReferencedClass(Class cls, String keyName, List<Field> fields){
        Set<Class> refedClasses = fields.stream()
                .map(f -> f.getAnnotation(FIDAttribute.class).cls())
                .filter(c ->  c != void.class)
                .collect(Collectors.toSet());
        switch (refedClasses.size()){
            case 0: return cls;
            case 1: return refedClasses.iterator().next();
            default:
                throw new RuntimeException(String.format(
                        "There are multiple referenced classes declared for Foreign ID \"%s\"" +
                                "in class \"%s\" in fields (%s) ", keyName, cls.getCanonicalName(),
                        fields.stream().map(f -> f.getName()).collect(Collectors.joining(", ")))
                );
        }
    }

    protected Pair<String, Field> createFKAttribute(Field f){
        String refedFieldName = f.getAnnotation(FIDAttribute.class).fieldRef();
        if(Constants.NO_FIELD_REFERENCE.equals(refedFieldName)){
            refedFieldName = f.getName();
        }
        return Pair.of(refedFieldName, f);
    }

    protected Pair<String, Field> createKeyAttribute(Class refedClass, Pair<String, Field> p){
        Field f = FieldUtils.getField(refedClass, p.getLeft(), true);
        if(f == null){
            throw new RuntimeException(String.format(
                            "Field \"%s\" not found in class \"%s\"",
                            p.getLeft(), refedClass.getCanonicalName()
            ));
        }
        return Pair.of(p.getLeft(), f);
    }


    ////////////////////////////////////////////////////////////////////
    // Extract Joins ///////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////
    public List<Join> extractJoins(Class refCls){
        List<Join> ret = new ArrayList<>();
        // find attributes of keys
        Map<FIDAttribute, Field> idAttributes = extractAnnotatedFields(refCls, FIDAttribute.class);

        // find fields that point to references
        Map<Relation, Field> refFields = extractAnnotatedFields(refCls, Relation.class);

        refFields.entrySet().forEach(e -> {
            Join j = createJoin(refCls, e.getKey(), e.getValue(),idAttributes);
            if(j != null){
                ret.add(j);
            }
        });
        return ret;
    }

    protected Join createJoin(Class refCls, Relation refA, Field refF, Map<FIDAttribute, Field> idAttributes ){
        // get the relevant idAttributes
        Class cls = refF.getType();
        Join j = new Join();
        j.setFrom(refCls);
        j.setTo(cls);
        j.setFromToMap(new ArrayList<>());

        idAttributes.entrySet().stream()
                .filter(e -> e.getKey().value().equals(refA.value()))
                .forEach(e -> {
                    Field from = e.getValue();
                    Field to = getMatchingField(cls, e.getKey(), e.getValue());
                    j.getFromToMap().add(Pair.of(from, to));
                });

        j.sortKey();
        return j;
    }

    protected Field getMatchingField(Class cls, FIDAttribute ida, Field f ){
        String fieldRef = ida.fieldRef();
        if(fieldRef.equals(Constants.NO_FIELD_REFERENCE)){
            fieldRef = f.getName();
        }

        try {
            return cls.getDeclaredField(fieldRef);
        } catch (NoSuchFieldException e) {
//            LOG.error("Field with name \"{}\" not found in class \"{}\"" , ref, cls.getCanonicalName(), e);
            throw new RuntimeException(e);
        }
    }

    protected <T extends Annotation> Map<T, Field> extractAnnotatedFields(Class cls, Class<T> annotationClass){
        return FieldUtils.getFieldsListWithAnnotation(cls, FIDAttribute.class)
                .stream().collect(Collectors.toMap(
                        f -> f.getAnnotation(annotationClass),
                        Function.identity()
                        ));

//        for(Field f: cls.getDeclaredFields()){
//            T a = f.getAnnotation(annotationClass);
//            if(a != null){
//                ret.put(a, f);
//            }
//        }
//        return ret;
    }

    ////////////////////////////////////////////////////////////////////
    // Data Model Classes //////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////

    public static void test1(){
        ExtractIDMetadata extractIDMetadata = new ExtractIDMetadata();
        List<Join> joins = extractIDMetadata.extractJoins(InstanceRef.class);
        joins.forEach(System.out::println);

        Instance i = new Instance("1","A","a");
        InstanceRef ir = new InstanceRef("A", "a");

        for(Join j : joins){
            System.out.println(j.calcFromId(ir));
            System.out.println(j.calcToId(i));
        }
    }

    public static void orderTwoLists(){
        List<String> l1 = new ArrayList<>(Arrays.asList("3","2","1"));
        List<String> l2 = new ArrayList<>(Arrays.asList("1","2","3"));

    }

    public static void main(String[] args) {
        Field f = FieldUtils.getField(C.class, "refs", true);
//        System.out.println(f.getType().getCanonicalName());
//        System.out.println(f.getType().getCanonicalName());
        System.out.println(((ParameterizedType)f.getGenericType()).getActualTypeArguments()[0]);
        System.out.println(((ParameterizedType)f.getGenericType()).getActualTypeArguments()[0].getTypeName());
        System.out.println(((ParameterizedType)f.getGenericType()).getRawType().getTypeName());
        System.out.println(((ParameterizedType)f.getGenericType()).getRawType());
        System.out.println(f.getType().getCanonicalName());
        System.out.println(f.getType().getGenericSuperclass());
        System.out.println(Arrays.toString(f.getType().getGenericInterfaces()));
//        System.out.println(((ParameterizedType)f.getGenericType()).getOwnerType());
//        System.out.println(((ParameterizedType)f.getGenericType()).getOwnerType().getTypeName());

        System.out.println(((ParameterizedType)FieldUtils.getField(C.class, "refs2", true).getGenericType()).getActualTypeArguments()[0]);

    }
}
