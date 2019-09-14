package cz.cvut.kbss.datatools.xmlanalysis.mapstructext.modules;

import cz.cvut.kbss.datatools.xmlanalysis.common.Utils;
import cz.cvut.kbss.datatools.xmlanalysis.common.refs.ReflectionUtils;
import cz.cvut.kbss.datatools.xmlanalysis.mapstructext.annotations.AddTypes;
import cz.cvut.kbss.datatools.xmlanalysis.mapstructext.annotations.AddTypesFromVal;
import cz.cvut.kbss.jopa.model.annotations.Types;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TypeDecorator implements ITypeDecorator {

    private static final Logger LOG = LoggerFactory.getLogger(TypeDecorator.class);

    protected Field field;
    protected List<Function<Object, List<String>>> typeGenerators = new ArrayList<>();

    public TypeDecorator(Field field) {
        this.field = field;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public List<Function<Object, List<String>>> getTypeGenerators() {
        return typeGenerators;
    }

    public void setTypeGenerators(List<Function<Object, List<String>>> typeGenerators) {
        this.typeGenerators = typeGenerators;
    }

    @Override
    public void decorate(Object in, Object out){
        Collection c = ReflectionUtils.getCollectionInstanceAndSetField(out, field);
        typeGenerators.forEach(f -> c.addAll(f.apply(in)));
    }

    public static TypeDecorator construct(Method transformDeclaration) {
        AddTypes addTypes = transformDeclaration.getAnnotation(AddTypes.class);
        AddTypesFromVal addTypesFromVal = transformDeclaration.getAnnotation(AddTypesFromVal.class);
        Field field = FieldUtils.getFieldsListWithAnnotation(transformDeclaration.getReturnType(), Types.class)
                .stream().findFirst().orElse(null);

        TypeDecorator td = null;
        if((addTypes != null || addTypesFromVal != null) && field != null) {
            td = new TypeDecorator(field);
            if(addTypes != null){
                td.getTypeGenerators().add(in -> getTypes(in, addTypes));
            }
            if(addTypesFromVal != null){
                td.getTypeGenerators().add(in -> getTypes(in, transformDeclaration, addTypesFromVal));
            }
        }
        return td;
    }

    public static List<String> getTypes(Object in, AddTypes addTypes){
        return Stream.of(addTypes.types()).collect(Collectors.toList());
    }

    public static List<String> getTypes(Object in, Method transformDeclaration, AddTypesFromVal addTypes){
        Class classOfInput = transformDeclaration.getParameterTypes()[0];
        Field f = FieldUtils.getField(classOfInput, addTypes.field(), true);
        try {// Future Feature ? - what if the Field is an array or Collection?
            return Arrays.asList(
                    addTypes.namespace() + Utils.urlEncode(Objects.toString(FieldUtils.readField(f, in)))
            );
        }catch(IllegalAccessException e){
            LOG.error("", e);
        }
        return Collections.emptyList();
    }
}
