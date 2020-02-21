package cz.cvut.kbss.datatools.xmlanalysis.common.refs;

import com.github.jsonldjava.utils.Obj;
import cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.jaxbmodel.BaseXMLEntity;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.Reference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReflectionUtils {
    private static final Logger LOG = LoggerFactory.getLogger(ReflectionUtils.class);

    public static List<Class> getGenericParameterClasses(Field f){
        try {
            List<Class> list = new ArrayList<>();
            for(Type t : ((ParameterizedType) f.getGenericType()).getActualTypeArguments()){
                list.add(Class.forName(t.getTypeName()));
            }
            return list;
        }catch (ClassNotFoundException e) {
            LOG.error("", e);
        }
        //
//        if(!Collection.class.isAssignableFrom(f.getType()))
//            throw new RuntimeException("Trying to process no collection")
//        Type[] types = ((ParameterizedType) f.getGenericType()).getActualTypeArguments();
//        if (types != null && types.length == 1){
//            try {
//                return Class.forName(types[0].getTypeName());
//            }catch (ClassNotFoundException e) {
//                LOG.error("", e);
//            }
//        }else{
//            LOG.error("More than one parameter in Collection");
//        }
        return null;
    }

    public static boolean isCollection(Field f){
        return Collection.class.isAssignableFrom(f.getType());
    }

    /**
     * Returns the field value, i.e. a collection instance. If the it is null a new collection instance with the correct
     * type is instantiated, set as the field value and returned.
     * @param inst
     * @param f
     * @return
     */
    public static Collection getCollectionInstanceAndSetField(Object inst, Field f){
        Collection targetCollection = (Collection)getValue(inst, f); // TODO get the collection instance of the getRelationField, or create a new collection based on the type
        if(targetCollection == null){
            targetCollection = getCollectionInstance(f);
            setValue(inst, f, targetCollection);
        }
        return targetCollection;
    }

    public static Collection getCollectionInstance(Field f){
        f.getType();
        Class elementType = getGenericParameterClasses(f).get(0);
        return getCollectionInstance(f.getType(), elementType);
    }

    public static <T> Collection<T> getCollectionInstance(Class collectionType, Class<T> elementType){
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

    public static void setValue(Object inst, Field f, Object value){
        try {
            f.setAccessible(true);
            f.set(inst, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getValue(Object inst, Field f){
        try {
            f.setAccessible(true);
            return f.get(inst);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    public static Set<Class> getClassClosure(Collection<Class> classes,
                                             BiFunction<Field, Collection<Class>, Collection<Class>> acceptableClasses){
        Set<Class> closure = new HashSet<>();
        Stack<Class> stack = new Stack<>();
        stack.addAll(classes);
        while(!stack.isEmpty()){
            Class cls = stack.pop();
            if(closure.contains(cls)) // class is already processed
                continue;

            closure.add(cls);

            // Search for other classes in the fields of "cls"
            for (Field f : FieldUtils.getAllFieldsList(cls)) {
                Collection<Class> fieldParams = new ArrayList<>();
                if (Iterable.class.isAssignableFrom(f.getType())) {
                    fieldParams.addAll(
                        Optional
                                .ofNullable(ReflectionUtils.getGenericParameterClasses(f))
                                .orElse(Collections.EMPTY_LIST)
                    );
                } else if (!f.getType().isPrimitive()) {
                    fieldParams.add(f.getType());
                }

                if( acceptableClasses != null) { // the class is not part of the extension
                    fieldParams = acceptableClasses.apply(f, fieldParams);
                }
                if(fieldParams != null)
                    stack.addAll(fieldParams);
            }
        }
        return closure;
    }

    public static void traverseGraph(Collection<Object> objects, BiConsumer<Object, Object> visitEdge){
        Set<Object> visited = new HashSet<>();
        Stack<Object> stack = new Stack<>();
        stack.addAll(objects);
        while(!stack.isEmpty()){
            Object obj = stack.pop();
            Class cls = obj.getClass();
            for (Field f : FieldUtils.getAllFieldsList(cls)) {
                if(f.getName().equals("parent") && f.getDeclaringClass().isAssignableFrom(BaseXMLEntity.class)){
                    continue; // ignore the parent field
                }
                Object val = ReflectionUtils.getValue(obj, f);
                if(val == null)
                    continue;

                if (Iterable.class.isAssignableFrom(val.getClass())) {
                    ((Iterable)val).forEach(v -> visitEdge.accept(obj, v));
                } else {
                    visitEdge.accept(obj, val);
                }
            }
        }
    }

    public static void main(String[] args) {
        Stack<Integer> s = new Stack();
        s.addAll(Arrays.asList(1,2,3));
        System.out.println("pop = " + s.pop());
        s.addAll(Arrays.asList(4,5,6));
        System.out.println("pop = " + s.pop());
        s.add(7);
        System.out.println("pop = " + s.pop());
        System.out.println("remaining elements in the stack : " +
                s.stream().map(i -> i + "").collect(Collectors.joining(", "))
        );
    }
//
//    public List<Method> getCurrentFilteredStack(Class<T> cls){
//        return getFilteredStack(Thread.currentThread(), cls);
//    }
//
//    public List<Method> getFilteredStack(Thread t, Class<T> cls){
//        List<Method> ret = new ArrayList<>();
//        for(StackTraceElement e : t.getStackTrace()){
//            e.
//            try {
//                Class c = Class.forName(e.getClassName());
//                if(cls.isAssignableFrom(c) || c.isAssignableFrom(cls)){
//                    Method m = MethodUtils.get
//                }
//            }catch (ClassNotFoundException ex) {
//                ex.printStackTrace();
//            }
//        }
//    }

}
