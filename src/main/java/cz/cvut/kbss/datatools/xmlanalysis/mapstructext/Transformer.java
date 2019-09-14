package cz.cvut.kbss.datatools.xmlanalysis.mapstructext;

import cz.cvut.kbss.datatools.xmlanalysis.mapstructext.modules.AttributeValuePredicate;
import cz.cvut.kbss.datatools.xmlanalysis.mapstructext.modules.TypeDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Predicate;

public class Transformer {

    private static final Logger LOG = LoggerFactory.getLogger(Transformer.class);

    protected Class mapperClass;
    protected Object mapper;
    protected Method transformDeclaration;
    protected Method transform;
    protected Class inputClass;
    protected TypeDecorator typeDecorator;
    protected Predicate isApplicable;


    public Transformer(Class mapperClass, Object mapper, Method transformDeclaration, Method transform) {
        this.mapperClass = mapperClass;
        this.mapper = mapper;
        this.transformDeclaration = transformDeclaration;
        this.transform = transform;
        this.inputClass = getTransformDeclaration().getParameterTypes()[0];
        typeDecorator = TypeDecorator.construct(transformDeclaration);
        isApplicable = AttributeValuePredicate.constructAttributeValueQualifier(transformDeclaration);
    }

    public Class getMapperClass() {
        return mapperClass;
    }

    public void setMapperClass(Class mapperClass) {
        this.mapperClass = mapperClass;
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


    public boolean isApplicableOn(Object in){
        return inputClass.isAssignableFrom(in.getClass()) &&
                (isApplicable != null ? isApplicable.test(in) : true);
    }

    public Object apply(Object in){
        if(in == null)
            return null;
        try {
            Object out = transform.invoke(mapper, in);
            if(typeDecorator != null && out != null){
                typeDecorator.decorate(in, out);
            }
            return out;
        } catch (IllegalAccessException e) {
            LOG.error("", e);
        } catch (InvocationTargetException e) {
            LOG.error("", e);
        }
        return null;
    }
}
