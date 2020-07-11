package cz.cvut.kbss.datatools.bpm2stampo.mapstructext.modules;

import cz.cvut.kbss.datatools.bpm2stampo.mapstructext.annotations.SetLabel;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class LabelDecorator implements Decorator{

    private static final Logger LOG = LoggerFactory.getLogger(IRIDecorator.class);


    protected Method transformDeclaration;
    protected SetLabel setLabelOnClass;
    protected SetLabel setLabel;
    protected String propertyIRI;
    protected Field sourceField;
    protected Field targetField;

    protected String labelPrefix = "";

    public Method getTransformDeclaration() {
        return transformDeclaration;
    }

    public void setTransformDeclaration(Method transformDeclaration) {
        this.transformDeclaration = transformDeclaration;
    }

    public SetLabel getSetLabelOnClass() {
        return setLabelOnClass;
    }

    public void setSetLabelOnClass(SetLabel setLabelOnClass) {
        this.setLabelOnClass = setLabelOnClass;
    }

    public SetLabel getSetLabel() {
        return setLabel;
    }

    public void setSetLabel(SetLabel setLabel) {
        this.setLabel = setLabel;
    }

    public String getPropertyIRI() {
        return propertyIRI;
    }

    public void setPropertyIRI(String propertyIRI) {
        this.propertyIRI = propertyIRI;
    }

    public Field getSourceField() {
        return sourceField;
    }

    public void setSourceField(Field sourceField) {
        this.sourceField = sourceField;
    }

    public Field getTargetField() {
        return targetField;
    }

    public void setTargetField(Field targetField) {
        this.targetField = targetField;
    }

    public String getLabelPrefix() {
        return labelPrefix;
    }

    public void setLabelPrefix(String labelPrefix) {
        this.labelPrefix = labelPrefix;
    }

    @Override
    public void configure(Method transformDeclaration){
        this.transformDeclaration = transformDeclaration;
        extractParameters();
        sourceField = extractSourceField(transformDeclaration);
        targetField = extractTargetField(transformDeclaration);
    }

    protected void extractParameters(){
        setLabel = transformDeclaration.getDeclaredAnnotation(SetLabel.class);
        Class cls = transformDeclaration.getDeclaringClass();
        setLabelOnClass = (SetLabel)cls.getDeclaredAnnotation(SetLabel.class);
        labelPrefix = constructPrefix(setLabel, setLabelOnClass);
        propertyIRI = extractPropertyIRI(setLabel, setLabelOnClass);
    }

    protected String extractPropertyIRI(SetLabel... setLabels){
        return getValue(SetLabel::propertyIRI, s -> !s.isEmpty(), "");
    }

    protected String constructPrefix(SetLabel... setLabels){

        String sep = getValue(SetLabel::sep, s -> !s.isEmpty(), "");

        String[] beforeLabel = getValue(SetLabel::beforeLabel, a -> a.length > 0, emptyStringArray);
        labelPrefix = String.join(sep, beforeLabel) + (beforeLabel.length == 0 ? "" : sep);

        return labelPrefix;
    }


    protected Field extractSourceField(Method transformDeclaration){
        String sourceFieldName = getValue(SetLabel::sourceField, s -> !s.isEmpty(), null);
//        String sourceFieldName = getValue(SetLabel::sourceField, s -> true, null);
        if(sourceFieldName == null)
            return null;

        return Stream.of(transformDeclaration.getParameterTypes())
                .map(c -> FieldUtils.getField(c, sourceFieldName,true))
                .filter(f -> f != null)
                .findFirst().orElse(null);
    }

    protected Field extractTargetField(Method transformDeclaration){
        String targetFieldName = getValue(SetLabel::targetField, s -> !s.isEmpty(), null);
        if(targetFieldName != null) {
            return FieldUtils.getField(transformDeclaration.getReturnType(), targetFieldName, true);
        }
        return FieldUtils.getFieldsListWithAnnotation(transformDeclaration.getReturnType(), OWLDataProperty.class)
                .stream()
                .filter(f -> f.getAnnotation(OWLDataProperty.class).iri().equals(propertyIRI))
                .findFirst().orElse(null);
    }


    @Override
    public void decorate(Object in, Object out){
        String val = calculateValue(in, out);
        try {
            if(targetField != null && val != null) {
                FieldUtils.writeField(targetField, out, val, true);
            }
        } catch (IllegalAccessException e) {
            LOG.error("", e);
        }
    }

    @Override
    public String calculateValue(Object in, Object out){
        try {
            String val = null;
            if(sourceField != null) {
                val = Objects.toString(FieldUtils.readField(sourceField, in));
                if(val != null)
                    val = val.trim();
                return String.format("%s%s", labelPrefix, val);
            }
        } catch (IllegalAccessException e) {
            LOG.error("", e);
        }
        return null;
    }

    protected <T> T getValue(Function<SetLabel, T> get, Predicate<T> condition, T defaultValue){
        return Stream.of(setLabel, setLabelOnClass)
                .filter(a -> a != null)
                .map(get)
                .filter(condition)
                .findFirst().orElse(defaultValue);
    }
}