package cz.cvut.kbss.datatools.xmlanalysis.common.refs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

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
}
