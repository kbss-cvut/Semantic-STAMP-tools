package cz.cvut.kbss.datatools.xmlanalysis.mapstructext;

import cz.cvut.kbss.datatools.xmlanalysis.mapstructext.modules.Decorator;
import cz.cvut.kbss.datatools.xmlanalysis.mapstructext.modules.TypeDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class Transformer {

    private static final Logger LOG = LoggerFactory.getLogger(Transformer.class);

    protected boolean rootTransformer;
    protected Class mapperDeclarationClass;
    protected Object mapper;
    protected Method transformDeclaration;
    protected Method transform;
    protected Class inputClass;
    protected TypeDecorator typeDecorator;
    protected List<Decorator> decorators = new ArrayList<>();
    protected Predicate isApplicable;


    public Transformer(Class mapperDeclarationClass, Object mapper, Method transformDeclaration, Method transform) {
        this.mapperDeclarationClass = mapperDeclarationClass;
        this.mapper = mapper;
        this.transformDeclaration = transformDeclaration;
        this.transform = transform;
        this.inputClass = getTransformDeclaration().getParameterTypes()[0];
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

    public void setMapper(Object mapper) {
        this.mapper = mapper;
    }

    public Method getTransformDeclaration() {
        return transformDeclaration;
    }

    public void setTransformDeclaration(Method transformDeclaration) {
        this.transformDeclaration = transformDeclaration;
    }

    public Method getTransform() {
        return transform;
    }

    public void setTransform(Method transform) {
        this.transform = transform;
    }

    public Class getInputClass() {
        return inputClass;
    }

    public void setInputClass(Class inputClass) {
        this.inputClass = inputClass;
    }

    public TypeDecorator getTypeDecorator() {
        return typeDecorator;
    }

    public void setTypeDecorator(TypeDecorator typeDecorator) {
        this.typeDecorator = typeDecorator;
    }

    public List<Decorator> getDecorators() {
        return decorators;
    }

    public void setDecorators(List<Decorator> decorators) {
        this.decorators = decorators;
    }

    public Predicate getIsApplicable() {
        return isApplicable;
    }

    public void setIsApplicable(Predicate isApplicable) {
        this.isApplicable = isApplicable;
    }

    public boolean isApplicableOn(Object in){
        return inputClass.isAssignableFrom(in.getClass()) &&
                (isApplicable != null ? isApplicable.test(in) : true);
    }

    public boolean isOneToMany(){
        return Collection.class.isAssignableFrom(transformDeclaration.getReturnType());
    }

    public Object apply(Object in){
        if(in == null)
            return null;
        try {
            return transform.invoke(mapper, in);
        } catch (IllegalAccessException e) {
            LOG.error("", e);
        } catch (InvocationTargetException e) {
            LOG.error("", e);
        }
        return null;
    }

    public void applyExtendedMappings(Object in, Object out){
        for(Decorator d : decorators){
            d.decorate(in, out);
        }
    }

    public boolean isRootTransformer() {
        return rootTransformer;
    }

    public void setRootTransformer(boolean rootTransformer) {
        this.rootTransformer = rootTransformer;
    }
}
