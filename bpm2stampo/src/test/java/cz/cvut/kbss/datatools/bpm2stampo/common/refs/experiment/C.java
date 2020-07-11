package cz.cvut.kbss.datatools.bpm2stampo.common.refs.experiment;

import cz.cvut.kbss.datatools.bpm2stampo.common.refs.annotations.*;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

public class C {
    public String id;

    @RelationType(relationType = RelationTypes.manyToOne)
    @FIDAttribute(cls = A.class, fieldRef = "id")
    public List<String> refs; // ids to A instances

    @RelationType(relationType = RelationTypes.manyToOne)
    public List<Aref> refs2;

    @Relation
    public List<A> as1;


    @Relation(instanceRef = "refs2")//, value = "1")
    public List<A> as2;

    public ArrayList<A> a;

    public C(String id, List<String> refs, List<Aref> refs2) {
        this.id = id;
        this.refs = refs;
        this.refs2 = refs2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getRefs() {
        return refs;
    }

    public void setRefs(List<String> refs) {
        this.refs = refs;
    }

    @Override
    public String toString() {
        return "C{" +
                "\n\tid='" + id + '\'' + "," +
                "\n\trefs=" + Optional.ofNullable(refs).map(r -> r.stream().collect(Collectors.joining(", "))).orElse(" -- ") +
                "\n\trefs2=" + Optional.ofNullable(refs2).map(r -> r.stream().map(x -> x.toString()).collect(Collectors.joining(", "))).orElse(" -- ") +
                "\n\tas1=" + Optional.ofNullable(as1).map(r -> r.stream().map(x -> x.toString()).collect(Collectors.joining(", "))).orElse(" -- ") +
                "\n\tas2=" + Optional.ofNullable(as2).map(r -> r.stream().map(x -> x.toString()).collect(Collectors.joining(", "))).orElse(" -- ") +
                '}';
    }

    public static Collection collectionInstance(Field f){
        try {
            f.getType();
            Class elementType = Class.forName(((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0].getTypeName());
            return collectionInstance(f.getType(), elementType);
        } catch (ClassNotFoundException e) {
//            LOG.error("",e);
        }
        return null;
    }

    public static <T> Collection<T> collectionInstance(Class collectionType, Class<T> elementType){
        try {
            return (Collection<T>)collectionType.newInstance();
        } catch (IllegalAccessException e) {
        } catch (InstantiationException e) {
        }

        // the instance could not be created
        if(Collection.class.isAssignableFrom(collectionType)){
            if(collectionType.isAssignableFrom(ArrayList.class)){
                return new ArrayList<>();
            }

            if(collectionType.isAssignableFrom(HashSet.class)){
                return new HashSet<>();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Field f = FieldUtils.getField(C.class, "a", true);
        Collection<A> c = collectionInstance(f);
        Collection<C> c1 = collectionInstance(f);

        System.out.println(c);
        System.out.println(c.hashCode());
        System.out.println(c.getClass());
        System.out.println(Arrays.asList(c.getClass().getTypeParameters()).stream().map(t -> Arrays.toString(t.getBounds())).collect(Collectors.joining()));
    }
}
