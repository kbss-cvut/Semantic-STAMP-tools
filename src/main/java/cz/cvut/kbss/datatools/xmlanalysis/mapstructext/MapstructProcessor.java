package cz.cvut.kbss.datatools.xmlanalysis.mapstructext;

import cz.cvut.kbss.datatools.xmlanalysis.mapstructext.annotations.AfterAllMappings;
import cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.BPMProcessor;
import cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.jaxbmodel.IBaseXMLEntity;
import cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.model.Identifiable;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class MapstructProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(MapstructProcessor.class);

    protected static Map<DefaultMapper, BPMProcessor> processorRegistry = new HashMap<>();

    protected Class<? extends DefaultMapper> mapperDeclarationClass;
    protected DefaultMapper mapper;
    protected List<Pair<Transformer, Class>> transformers;

    protected Stack<TransformExecution> transformStack = new Stack<>();

    public Stack<TransformExecution> getTransformStack() {
        return transformStack;
    }

    public void setTransformStack(Stack<TransformExecution> transformStack) {
        this.transformStack = transformStack;
    }

    public void registerProcessor(DefaultMapper key, BPMProcessor processor){
        processorRegistry.put(key, processor);
    }

    public BPMProcessor getProcessor(DefaultMapper key){
        return processorRegistry.get(key);
    }

    public MapstructProcessor(Class<? extends DefaultMapper> mapperDeclarationClass) {
        this.mapperDeclarationClass = mapperDeclarationClass;
        this.mapper = Mappers.getMapper(mapperDeclarationClass);
        transformers = MappingUtils.rootMappingTransformers(mapperDeclarationClass, mapper);
    }

    public Class getMapperDeclarationClass() {
        return mapperDeclarationClass;
    }

    public void setMapperDeclarationClass(Class mapperDeclarationClass) {
        this.mapperDeclarationClass = mapperDeclarationClass;
    }

    public Object getMapper() {
        return mapper;
    }

    public void setMapper(DefaultMapper mapper) {
        this.mapper = mapper;
    }

    public List<Pair<Transformer, Class>> getTransformers() {
        return transformers;
    }

    public void setTransformers(List<Pair<Transformer, Class>> transformers) {
        this.transformers = transformers;
    }

    public List<Object> transformAll(Collection<Object> objectsIn){
//        List<Object> objects = new ArrayList<>(objectsIn.size());
        for(Object in : objectsIn){
            if(in == null)
                continue; // filter null elements in the input
            List<Object> out = transform(in);
            if(out != null){
//                objects.addAll((Collection)out);
            }else{
                LOG.warn("Failed to transform object of class '{}'. toString = '{}'", in.getClass(), out);
            }
        }
        afterAllMappingExecution();
//        ArrayList<Object> ret = new ArrayList<>(mapper.getRegistry().values());
//        ret.addAll(objects);
//        return ret;
        return new ArrayList<>(mapper.getRegistry().values());
    }

    protected void initParents(Collection<Object> objectsIn){
        objectsIn.forEach(this::initParents);
    }

    protected void initParents(Object obj){
        if(obj instanceof IBaseXMLEntity){
            IBaseXMLEntity iBaseXMLEntity = (IBaseXMLEntity)obj;
//            iBaseXMLEntity.
        }
    }

    public void afterAllMappingExecution(){
        List<Method> methods = MethodUtils.getMethodsListWithAnnotation(mapperDeclarationClass, AfterAllMappings.class);
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

    public List<Object> transform(Object in) {
        List<Transformer> transformers = this.transformers.stream()
                .filter(tp -> tp.getKey().isRootTransformer())
                .filter(tp -> tp.getValue().isAssignableFrom(in.getClass()))
                .map(tp -> tp.getKey())
                .collect(Collectors.toList());
        return transform(in, transformers);
    }

    public List<Object> transform(Object in, List<Transformer> transformers){
        transformers = transformers.stream()
                .filter(t -> t.isApplicableOn(in))
//                .findFirst()
                .filter(t -> t != null)
                .collect(Collectors.toList());
        if(!transformers.isEmpty()){
            List<Object> ret = new ArrayList<>();
            for(Transformer t : transformers) {
                transformStack.push(new TransformExecution(t, in));
                try {
                    Object out = t.apply(in);
                    if (t.isOneToMany()) {
                        ret.addAll((Collection) out);
                    } else
                        ret.add(out);
                }finally {
                    transformStack.pop();
                }
            }
            return ret;
        }else{
            LOG.warn("Could not find transform method in class '{}' for class '{}'",
                    mapper.getClass().getCanonicalName(),
                    in.getClass().getCanonicalName()
            );
        }
        return null;
    }

    /**
     * This method executes a transformer and makes sure to maintain the stack of transformer executions.
     * This method is called trough the {@link DefaultMapper#transform} method and it should not be called directly.
     * @param methodName
     * @param e
     * @param outClass
     * @param <T>
     * @return
     */
    public <T extends Identifiable> T transform(String methodName, Object e, Class<T> outClass){
        Method declarationMethod = MethodUtils.getMatchingMethod(mapperDeclarationClass, methodName, e.getClass()); // Vulnerable : Will it work if e is proxied for some reason?
        if(declarationMethod == null)
            return null;
        Transformer t = getTransformer(declarationMethod);
        return (T)transform(e, Arrays.asList(t));
//        if(t == null){
//            return null;
//        }
//
//        TransformExecution te = new TransformExecution(t, e);
//        transformStack.push(te);
//        T ret = null;
//        try {
//             ret = (T)methodToExecute.invoke(mapper, e);
//        } catch (IllegalAccessException ex) {
//            ex.printStackTrace();
//        } catch (InvocationTargetException ex) {
//            ex.printStackTrace();
//        }finally {
//            transformStack.pop();
//        }
//        return ret;
    }

    public Transformer getTransformer(Method m){
        return transformers.stream()
                .filter(tp -> tp.getKey().getTransformDeclaration().equals(m))
                .map(tp -> tp.getKey())
                .findFirst() // Note: this won't support transformers based on multi argument methods
                .orElse(null);
    }

//    protected TransformExecution pushTransformer(Method m, BaseXMLEntity e){
//
//        if(t != null) {
//
//            TransformExecution te = new TransformExecution(t, e);
//            transformStack.push(te);
//            return te;
//        }
//        return null;
//    }

//    protected TransformExecution popTransformer(){
//        return transformStack.pop();
//    }
}
