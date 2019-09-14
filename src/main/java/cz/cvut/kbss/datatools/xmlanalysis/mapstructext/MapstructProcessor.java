package cz.cvut.kbss.datatools.xmlanalysis.mapstructext;

import cz.cvut.kbss.datatools.xmlanalysis.mapstructext.annotations.AfterAllMappings;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MapstructProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(MapstructProcessor.class);

    protected Class mapperClass;
    protected Object mapper;
    protected List<Pair<Transformer, Class>> transformers;


    public MapstructProcessor(Class mapperClass) {
        this.mapperClass = mapperClass;
        this.mapper = Mappers.getMapper(mapperClass);
        transformers = MappingUtils.rootMappingTransformers(mapperClass, mapper);
    }


    public List<Object> transformAll(Collection<Object> objectsIn){
        List<Object> objects = new ArrayList<>(objectsIn.size());
        for(Object in : objectsIn){
            if(in == null)
                continue; // filter null elements in the input
            Object out = transform(in);
            if(out != null){
                objects.add(out);
            }else{
                LOG.warn("Failed to transform object of class '{}'. toString = '{}'", in.getClass(), out);
            }
        }
        afterAllMappingExecution();
        return objects;
    }

    public void afterAllMappingExecution(){
        List<Method> methods = MethodUtils.getMethodsListWithAnnotation(mapperClass, AfterAllMappings.class);
        for(Method m : methods){
            try {
                m.invoke(mapper);
            }catch(IllegalAccessException e){
                LOG.error("", e);
            } catch (InvocationTargetException e) {
                LOG.error("", e);
            }
        }
    }

    public Object transform(Object in){
        Transformer transform = transformers.stream()
                .filter(t -> t.getValue().isAssignableFrom(in.getClass()))
                .filter(t -> t.getKey().isApplicableOn(in))
                .findFirst().map(p -> p.getLeft()).orElse(null);
        if(transform != null){
            return transform.apply(in);
        }else{
            LOG.warn("Could not find transform method in class '{}' for class '{}'",
                    mapper.getClass().getCanonicalName(),
                    in.getClass().getCanonicalName()
            );
        }
        return null;
    }





}
