package cz.cvut.kbss.datatools.bpm2stampo.common.refs;


import cz.cvut.kbss.datatools.bpm2stampo.common.refs.annotations.*;
import cz.cvut.kbss.datatools.bpm2stampo.common.refs.model.*;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Set<Class> classesToProcess = ReflectionUtils.getClassClosure(classes, null);
        classesToProcess.stream().forEach(this::buildClassKeyMappingMetadata);
        classesToProcess.stream().forEach(this::buildClassRelationMetadata);
        return schema;
    }


    protected Set<Class> extractAdditionalClassToProcess(Collection<Class> classes){
        Set<Class> additionalClassesToProcess = new HashSet<>();
        for(Class cls : classes) {
            // Search for other classes in the fields of "cls"

            for (Field f : FieldUtils.getAllFieldsList(cls)) {
                if (Iterable.class.isAssignableFrom(f.getType())) {
                    Optional.ofNullable(ReflectionUtils.getGenericParameterClasses(f))
                            .ifPresent(additionalClassesToProcess::addAll);
                } else if (!f.getType().isPrimitive()) {
                    additionalClassesToProcess.add(f.getType());
                }
            }
        }

        return additionalClassesToProcess;
    }

    ////////////////////////////////////////////////////////////////////
    // Extract relations ///////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////

    public void buildClassRelationMetadata(Class cls){
        ClassMetadata clsMetadata = getClassMetadata(cls);
        clsMetadata.setRelations(extractRelationalFields(cls));
        for(RelationField rf : extractFarRelationalFields(cls)){
            Class fkSideClass = rf.getKeyMapping().getForeignKey().getCls();
            ClassMetadata fkSideClassMetadata = getClassMetadata(fkSideClass);
            fkSideClassMetadata.getFarRelations().add(rf);
        }
    }

    // Near Relations //////////////////////////////////////////////////
    protected Set<RelationField> extractRelationalFields(Class cls){
        return FieldUtils.getFieldsListWithAnnotation(cls, Relation.class)
                .stream()
                .map(f -> {
                    Relation r = f.getAnnotation(Relation.class);
                    return createRelationField(cls, r.value(), r.instanceRef(), f);
                })
                .collect(Collectors.toSet());

//        return null; // TODO
    }

    protected RelationField createRelationField(Class cls, String keyName, String keyContainerField, Field relationField){
        RelationField relation = new RelationField();
        relation.setRelationField(relationField);
        relation.setManyRelationField(ReflectionUtils.isCollection(relationField));
        Class fkContainer = null;


        if(Constants.NO_INSTANCE_REFERENCE.equals(keyContainerField)){
            fkContainer = cls;
            ClassMetadata clsMetadata = schema.getClass2MetadataMap().get(fkContainer);
            KeyMapping keyMapping = clsMetadata.getKeyMapping(keyName);
            relation.setRelationType(keyMapping.getRelationType());
        }else{
            Field containerField = FieldUtils.getField(cls, keyContainerField, true);
            relation.setContainerField(containerField);
            RelationType relationType = containerField.getAnnotation(RelationType.class);
            if(relationType == null) {
                relation.setRelationType(RelationTypes.oneToOne);
            }else {
                relation.setRelationType(relationType.relationType());
            }
            if(relation.getRelationType() == RelationTypes.manyToOne){
//                fkContainer = containerField.getType();
                if(ReflectionUtils.isCollection(containerField)) {
                    fkContainer = ReflectionUtils.getGenericParameterClasses(containerField).get(0);
                }else{
                    throw new RuntimeException(String.format("Cannot use ManyFK on non Collection field '%s' in class '%s'.", containerField.getName(), cls.getCanonicalName()));
                }
//                ((ParameterizedType)f.getGenericType()).getActualTypeArguments()
            }else{
                fkContainer = containerField.getType();
            }
        }

        boolean isCollectionRelation = relation.isManyRelationField();

        if(relation.getRelationType() == RelationTypes.manyToOne && !isCollectionRelation){
            throw new RuntimeException(String.format("A single fields relation refers to a manyFK field, relation field '%s' in class '%s' .", relationField.getName(), cls.getCanonicalName()));
        }

        if(relation.getRelationType() == RelationTypes.oneToMany && isCollectionRelation){
            throw new RuntimeException(String.format("A collection relation fields refers to a one to many key mapping, relation field '%s' in class '%s' .", relationField.getName(), cls.getCanonicalName()));
        }


        if(relation.getRelationType() == RelationTypes.oneToOne && isCollectionRelation){
            throw new RuntimeException(String.format("A collection relation field refers to a one to one key mapping, see field '%s' in class '%s'.", relationField.getName(), cls.getCanonicalName()));
        }

        if(relation.getRelationType() == RelationTypes.manyToMany ){
            throw new RuntimeException("many to many relations not supported yet!");
        }

        ClassMetadata clsMetadata = schema.getClass2MetadataMap().get(fkContainer);
        KeyMapping keyMapping = clsMetadata.getKeyMapping(keyName);
        relation.setKeyMapping(keyMapping);
        return relation;
    }


    // far relations ///////////////////////////////////////////////////
    protected Set<RelationField> extractFarRelationalFields(Class cls){
        return FieldUtils.getFieldsListWithAnnotation(cls, FarRelation.class)
                .stream()
                .map(f -> {

                    FarRelation r = f.getAnnotation(FarRelation.class);
                    return createFarRelationField(cls, r.value(), f);
                })
                .filter(f -> f != null)
                .collect(Collectors.toSet());

//        return null; // TODO
    }

    protected RelationField createFarRelationField(Class cls, String keyName, Field relationField){
        Class fkContainer = null;

        boolean isCollectionRelation = ReflectionUtils.isCollection(relationField);
        if(isCollectionRelation) {
            fkContainer = ReflectionUtils.getGenericParameterClasses(relationField).get(0);
        }else{
            fkContainer = relationField.getType();
        }

        ClassMetadata fkContainerMetadata = schema.getClass2MetadataMap().get(fkContainer);
        if(fkContainerMetadata == null){
            LOG.warn("Cannot construct far relation defined on field {} in class {}. No class metadata found for far side class {}.",
                    relationField.getName(),
                    cls.getCanonicalName(),
                    fkContainer.getCanonicalName()
            );
            return null;
        }

        KeyMapping keyMapping = fkContainerMetadata.getKeyMapping(keyName);
        if(keyMapping == null){
            LOG.warn("Cannot construct far relation defined on field {} in class {}. No key with name {} found in class {}.",
                    relationField.getName(),
                    cls.getCanonicalName(),
                    keyName,
                    fkContainer.getCanonicalName()
            );
            return null;
        }

        RelationField farRelationField = new RelationField();
        farRelationField.setManyRelationField(isCollectionRelation);
        farRelationField.setRelationField(relationField);

        farRelationField.setKeyMapping(keyMapping);
        farRelationField.setRelationType(keyMapping.getRelationType());



        if(farRelationField.getRelationType() == RelationTypes.manyToOne && !isCollectionRelation){
            throw new RuntimeException(String.format("A single fields relation refers to a manyFK field, relation field '%s' in class '%s' .", relationField.getName(), cls.getCanonicalName()));
        }

        if(farRelationField.getRelationType() == RelationTypes.oneToMany && isCollectionRelation){
            throw new RuntimeException(String.format("A collection relation fields refers to a one to many key mapping, relation field '%s' in class '%s' .", relationField.getName(), cls.getCanonicalName()));
        }

        if(farRelationField.getRelationType() == RelationTypes.oneToOne && isCollectionRelation){
            throw new RuntimeException(String.format("A collection relation field refers to a one to one key mapping, see field '%s' in class '%s'.", relationField.getName(), cls.getCanonicalName()));
        }

        if(farRelationField.getRelationType() == RelationTypes.manyToMany){
            throw new RuntimeException("Many to many relations not supported yet!");
        }

        return farRelationField;
    }
//    protected void

    ////////////////////////////////////////////////////////////////////
    // Extract KeyMappings /////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////

    protected void buildClassKeyMappingMetadata(Class cls){
        if(processedClasses.contains(cls)){
            return;
        }

        ClassMetadata classMetadata = getClassMetadata(cls);

        List<KeyMapping> keyMappings = extractForeignKeys(cls);
        classMetadata.setKeyMappings(new HashSet<>(keyMappings));
        // Associate Keys with their classes
        // TODO : remove duplicate keys.
        for(KeyMapping m : keyMappings){
            Key k = m.getKey();
            ClassMetadata clsMetadata = getClassMetadata(k.getCls());
            clsMetadata.getToOneKeys().add(k);
//            if(m.getRelationType() == RelationTypes.manyToOne || m.getRelationType() == RelationTypes.oneToOne) {
//                clsMetadata.getToOneKeys().add(k);
//            }else{
//                clsMetadata.getOneToManyKeys().add(k);
//            }

        }
        processedClasses.add(cls);

    }

    protected ClassMetadata getClassMetadata(Class cls){
        ClassMetadata clsMetadata = schema.getClass2MetadataMap().get(cls);
        if(clsMetadata == null){
            clsMetadata = new ClassMetadata();
            clsMetadata.setToOneKeys(new HashSet<>());
            schema.getClass2MetadataMap().put(cls, clsMetadata);
        }
        return clsMetadata;
    }

    protected List<KeyMapping> extractForeignKeys(Class cls){
        List<KeyMapping> ret = new ArrayList<>();
//        List<Field> allKeyAttributeFields = FieldUtils.getFieldsListWithAnnotation(cls, FIDAttribute.class);
        List<Pair<FIDAttribute, Field>> allKeyAttributeFields = listKeyAttributes(cls);
        Predicate<Field> isManyToOneFK = f -> f.getAnnotation(ManyToOneFK.class) != null;

        List<KeyMapping> one2oneKMs = extractKMs(cls, allKeyAttributeFields, false, false);
        ret.addAll(one2oneKMs);

        List<KeyMapping> many2oneKMs = extractKMs(cls, allKeyAttributeFields, true, false);
        ret.addAll(many2oneKMs);

        List<KeyMapping> one2manyKMs = extractKMs(cls, allKeyAttributeFields, true, false);
        ret.addAll(one2manyKMs);
        return ret;
    }

    protected List<KeyMapping> extractKMs(Class cls, List<Pair<FIDAttribute, Field>> allKeyAttributeFields, boolean m2o, boolean o2m){
        return allKeyAttributeFields.stream()
                .filter(p ->
                                (p.getRight().getAnnotation(ManyToOneFK.class) != null) == m2o &&
                                (p.getRight().getAnnotation(OneToManyFK.class) != null) == o2m
                        )
                .collect(// group fields by key ID
                        Collectors.groupingBy(p -> p.getLeft().value())
                ).entrySet().stream()
                .map(e -> createKeyMapping(cls, e.getKey(), e.getValue(), m2o, o2m))
                .collect(Collectors.toList());
    }

    protected List<Pair<FIDAttribute, Field>> listKeyAttributes(Class cls){
        List<Field> fieldWithOneAttribute = FieldUtils.getFieldsListWithAnnotation(cls, FIDAttribute.class);
        List<Field> fieldWithMultipleAttributes = FieldUtils.getFieldsListWithAnnotation(cls, FIDAttributes.class);
        return Stream.concat(
            fieldWithOneAttribute.stream()
                    .map(f -> Pair.of(f.getAnnotation(FIDAttribute.class), f)),
            fieldWithMultipleAttributes.stream()
                    .flatMap(f -> Stream.of(f.getAnnotation(FIDAttributes.class).value()).map(a -> Pair.of(a, f)))
        ).collect(Collectors.toList());
    }



    protected KeyMapping createKeyMapping(Class cls, String keyName, List<Pair<FIDAttribute,Field>> fields, boolean isManyToOneFK, boolean isOneToManyFK){
        // check consistency
        if (isManyToOneFK && fields.size() > 1) {
            throw new RuntimeException(String.format(
                    "could not construct a manyFK mapping in class '%s', there are more" +
                            " then one attributes associated with one attrubute per keyName = '%s'",
                    cls.getCanonicalName(), keyName
            ));
        }

        // extract and prepare keys to construct a KeyMapping
        Class refedClass = extractReferencedClass(cls, keyName, fields);

        // TODO : do not create duplicate keys.
        // create foreign Key
        Key fkey = new Key();
        fkey.setKeyName(keyName);
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


        KeyMapping keyMapping = new KeyMapping(fkey, key, isManyToOneFK, isOneToManyFK);

        return keyMapping;

    }

    protected Class extractReferencedClass(Class cls, String keyName, List<Pair<FIDAttribute, Field>> fields){
        Set<Class> refedClasses = fields.stream()
                .map(p -> p.getLeft().cls())
                .filter(c ->  c != void.class)
                .collect(Collectors.toSet());
        switch (refedClasses.size()){
            case 0: return cls;
            case 1: return refedClasses.iterator().next();
            default:
                throw new RuntimeException(String.format(
                        "There are multiple referenced classes declared for Foreign ID \"%s\"" +
                                "in class \"%s\" in fields (%s) ", keyName, cls.getCanonicalName(),
                        fields.stream().map(p -> p.getRight().getName()).collect(Collectors.joining(", ")))
                );
        }
    }

    protected Pair<String, Field> createFKAttribute(Pair<FIDAttribute,Field> p){
        String refedFieldName = p.getLeft().fieldRef();
        if(Constants.NO_FIELD_REFERENCE.equals(refedFieldName)){
            refedFieldName = p.getRight().getName();
        }
        return Pair.of(refedFieldName, p.getRight());
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
    @Deprecated
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
    }
}
