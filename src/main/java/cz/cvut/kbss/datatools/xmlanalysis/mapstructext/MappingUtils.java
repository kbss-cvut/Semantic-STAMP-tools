package cz.cvut.kbss.datatools.xmlanalysis.mapstructext;

import cz.cvut.kbss.datatools.xmlanalysis.mapstructext.annotations.RootMapping;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MappingUtils {
    protected static final Comparator<Class> comp = (a, b) -> a.equals(b) ? 0 : a.isAssignableFrom(b) ? 1 : -1;

    public static List<Pair<Transformer, Class>> rootMappingTransformers(Class mapperClass, Object mapper){
        return Stream.of(mapperClass.getMethods())
                .filter(m -> m.isAnnotationPresent(RootMapping.class))
                .filter(m -> m.getParameterCount() == 1)// Future feature???
                .map(m -> Pair.of(m, MethodUtils.getMatchingMethod(mapper.getClass(), m.getName(), m.getParameterTypes())))
                .filter(p -> p.getRight() != null)
                .map(p -> new Transformer(mapperClass, mapper, p.getLeft(), p.getRight()) )
                .flatMap(tr -> Stream.of((Class[])tr.getTransformDeclaration().getParameterTypes()).map(c -> Pair.of(tr, c)))
                .sorted((a, b) -> comp.compare(a.getRight(), b.getRight()))
                .collect(Collectors.toList());
    }

}
