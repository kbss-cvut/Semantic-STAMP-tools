package cz.cvut.kbss.datatools.xmlanalysis.mapstructext.modules;

import cz.cvut.kbss.datatools.xmlanalysis.mapstructext.annotations.AttributeValueQualifier;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;


/**
 * Implements the {@link cz.cvut.kbss.datatools.xmlanalysis.mapstructext.annotations.AttributeValueQualifier} annotation.
 */
public class AttributeValuePredicate<T> implements Predicate<T> {

    private static final Logger LOG = LoggerFactory.getLogger(AttributeValuePredicate.class);

    protected Field discriminatorField;
    protected Set<String> possibleValues;

    public AttributeValuePredicate() {
    }

    public AttributeValuePredicate(Field discriminatorField, Set<String> possibleValues) {
        this.discriminatorField = discriminatorField;
        this.possibleValues = possibleValues;
    }

    public Field getDiscriminatorField() {
        return discriminatorField;
    }

    public void setDiscriminatorField(Field discriminatorField) {
        this.discriminatorField = discriminatorField;
    }

    public Set<String> getPossibleValues() {
        return possibleValues;
    }

    public void setPossibleValues(Set<String> possibleValues) {
        this.possibleValues = possibleValues;
    }

    @Override
    public boolean test(T in) {
        try {
            String val = Objects.toString(FieldUtils.readField(discriminatorField, in));
            return possibleValues.contains(val);
        } catch (IllegalAccessException e) {
            LOG.error("",e);
        }
        return false;
    }

    public static AttributeValuePredicate constructAttributeValueQualifier(Method transformDeclaration){
        AttributeValueQualifier attributeValueQualifier = transformDeclaration.getAnnotation(AttributeValueQualifier.class);
        if(attributeValueQualifier != null) {
            // find class and field
            Class classOfInput = transformDeclaration.getParameterTypes()[0];
            Field f = FieldUtils.getField(classOfInput, attributeValueQualifier.field(), true);
            Set<String> vals = new HashSet<>(Arrays.asList(attributeValueQualifier.acceptedValues()));
            if (f != null) {
                return new AttributeValuePredicate(f, vals);
            }
        }
        return null;
    }
}
