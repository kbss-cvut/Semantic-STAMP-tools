package cz.cvut.kbss.datatools.xmlanalysis.mapstructext;

import cz.cvut.kbss.datatools.xmlanalysis.mapstructext.annotations.*;
import cz.cvut.kbss.datatools.xmlanalysis.mapstructext.modules.*;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MappingUtils {

    private static final Logger LOG = LoggerFactory.getLogger(MappingUtils.class);

    protected static final Comparator<Class> comp = (a, b) -> a.equals(b) ? 0 : a.isAssignableFrom(b) ? 1 : -1;

    public static Map<Class<? extends Annotation>, Class<? extends Decorator>> annotationDecoratorMap;
    static {
        annotationDecoratorMap = new HashMap<>();
        annotationDecoratorMap.put(AddTypes.class, TypeDecorator.class);
        annotationDecoratorMap.put(AddTypesFromVal.class, TypeDecorator.class);
        annotationDecoratorMap.put(SetIRI.class, IRIDecorator.class);
        annotationDecoratorMap.put(SetLabel.class, LabelDecorator.class);
    }

    public static List<Pair<Transformer, Class>> rootMappingTransformers(Class mapperDeclarationClass, Object mapper){
        return Stream.of(mapperDeclarationClass.getMethods())
//                .filter(m -> m.isAnnotationPresent(RootMapping.class))
                .filter(m -> m.getParameterCount() == 1)// Future feature???
                .map(m -> Pair.of(m, getMatchingMethod(mapper.getClass(), m)))
                .filter(p -> p.getRight() != null)
                .map(p -> constructTransformer(mapperDeclarationClass, mapper, p.getLeft(), p.getRight()) )
                .flatMap(tr -> Stream.of((Class[])tr.getTransformDeclaration().getParameterTypes()).map(c -> Pair.of(tr, c)))
                .sorted((a, b) -> comp.compare(a.getRight(), b.getRight()))
                .collect(Collectors.toList());
    }

    public static Method getMatchingMethod(Class mapperClass, Method  m){
        Method match = MethodUtils.getMatchingMethod(mapperClass, m.getName(), m.getParameterTypes());
        if(match == null && m.isDefault())
            match = m;
        return match;
    }

    public static Transformer constructTransformer(Class mapperDeclarationClass, Object mapper, Method transformDeclaration, Method transform){
        Transformer t = new Transformer(mapperDeclarationClass, mapper, transformDeclaration, transform);
        annotationDecoratorMap.entrySet().forEach(e ->{
            Annotation a = transformDeclaration.getAnnotation(e.getKey());
            if(a == null) {
                a = transformDeclaration.getDeclaringClass().getAnnotation(e.getKey());
                if(a == null)
                    return;
            }
            Decorator decorator = createDecorator(e.getValue(), transformDeclaration);
            if(decorator != null)
                t.getDecorators().add(decorator);
        });

        // TODO: set transform predicate
        Predicate isApplicable = AttributeValuePredicate.constructAttributeValueQualifier(transformDeclaration);
        if(isApplicable != null)
            t.setIsApplicable(isApplicable);

        t.setRootTransformer(transformDeclaration.isAnnotationPresent(RootMapping.class));
        return t;
    }

    public static Decorator createDecorator(Class<? extends Decorator> decoratorClass, Method transformDeclaration){

        Decorator decorator = null;

        try {
            decorator = decoratorClass.newInstance();
        } catch (InstantiationException e) {
            LOG.error("", e);
        } catch (IllegalAccessException e) {
            LOG.error("", e);
        }
        if(decorator != null){
            decorator.configure(transformDeclaration);
        }
        return decorator;
    }

}
