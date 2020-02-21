package cz.cvut.kbss.datatools.xmlanalysis.mapstructext.modules;

import cz.cvut.kbss.datatools.xmlanalysis.mapstructext.annotations.SetIRI;
import cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.JAXBUtils;
import cz.cvut.kbss.jopa.model.annotations.Id;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IRIDecorator implements Decorator{

    private static final Logger LOG = LoggerFactory.getLogger(IRIDecorator.class);

    protected Method transformDeclaration;
    protected SetIRI setIRIOnClass;
    protected SetIRI setIRI;
    protected List<Field> sourceFields;
    protected Field targetField;

    protected String sep = "";
    protected String iriBase = "";
    protected String beforeId = "";
    protected String afterId = "";

    protected String iriPrefix = "";
    protected String iriPostfix = "";


    public String getSep() {
        return sep;
    }

    public void setSep(String sep) {
        this.sep = sep;
    }

    public String getIriBase() {
        return iriBase;
    }

    public void setIriBase(String iriBase) {
        this.iriBase = iriBase;
    }

    public String getBeforeId() {
        return beforeId;
    }

    public void setBeforeId(String beforeId) {
        this.beforeId = beforeId;
    }

    public boolean equalsBeforeId(String[] beforeId){
        return Stream.of(beforeId).collect(Collectors.joining(sep)).equals(this.beforeId);
    }

    public String getAfterId() {
        return afterId;
    }

    public void setAfterId(String afterId) {
        this.afterId = afterId;
    }

    public boolean equalsAfterId(String[] afterId){
        return Stream.of(afterId).collect(Collectors.joining(sep)).equals(this.afterId);
    }

    public Method getTransformDeclaration() {
        return transformDeclaration;
    }

    public void setTransformDeclaration(Method transformDeclaration) {
        this.transformDeclaration = transformDeclaration;
    }

    public SetIRI getSetIRIOnClass() {
        return setIRIOnClass;
    }

    public void setSetIRIOnClass(SetIRI setIRIOnClass) {
        this.setIRIOnClass = setIRIOnClass;
    }

    public SetIRI getSetIRI() {
        return setIRI;
    }

    public void setSetIRI(SetIRI setIRI) {
        this.setIRI = setIRI;
    }

    public List<Field> getSourceFields() {
        return sourceFields;
    }

    public void setSourceFields(List<Field> sourceFields) {
        this.sourceFields = sourceFields;
    }

    public Field getTargetField() {
        return targetField;
    }

    public void setTargetField(Field targetField) {
        this.targetField = targetField;
    }

    public String getIriPrefix() {
        return iriPrefix;
    }

    public void setIriPrefix(String iriPrefix) {
        this.iriPrefix = iriPrefix;
    }

    @Override
    public void configure(Method transformDeclaration){
        this.transformDeclaration = transformDeclaration;
        extractParameters();
        sourceFields = extractSourceFields(transformDeclaration);
        targetField = extractTargetField(transformDeclaration);
    }


    protected void extractParameters(){
        setIRI = transformDeclaration.getDeclaredAnnotation(SetIRI.class);
        Class cls = transformDeclaration.getDeclaringClass();
        setIRIOnClass = (SetIRI)cls.getDeclaredAnnotation(SetIRI.class);
        iriPrefix = constructPrefixPostfix(setIRI, setIRIOnClass);
    }

    protected String constructPrefixPostfix(SetIRI ... setIRIs){
        sep = getValue(SetIRI::sep, s -> !s.isEmpty(), "");

        iriBase = getValue(SetIRI::prefix, s -> !s.isEmpty(), "");

        beforeId = String.join(sep, getValue(SetIRI::beforeId, s -> s.length > 0, emptyStringArray)).trim();

        afterId = String.join(sep, getValue(SetIRI::afterId, s -> s.length > 0, emptyStringArray)).trim();

        iriPrefix = iriBase + beforeId + (beforeId.isEmpty() ? "" : sep);
        iriPostfix = (afterId.isEmpty() ? "" : sep) + afterId;
        return iriPrefix;
    }


    protected List<Field> extractSourceFields(Method transformDeclaration){
//        String sourceFieldName = getValue(SetIRI::sourceField, s -> !s.isEmpty(), null);
        String sourceFieldName = getValue(SetIRI::sourceField, s -> true, null);

        if(sourceFieldName == null || sourceFieldName.isEmpty())
            return Stream.of(transformDeclaration.getParameterTypes())
                    .map(c -> JAXBUtils.extractJAXBIdFields(c))
                    .filter(l -> !l.isEmpty())
                    .findFirst()
                    .orElse(null);

        return Arrays.asList(
                Stream.of(transformDeclaration.getParameterTypes())
                .map(c -> FieldUtils.getField(c, sourceFieldName,true))
                .filter(f -> f != null)
                .findFirst().orElse(null)
        );
    }

    protected Field extractTargetField(Method transformDeclaration){
        return FieldUtils.getFieldsListWithAnnotation(transformDeclaration.getReturnType(), Id.class)
                .stream().findFirst().orElse(null);
    }

    @Override
    public void decorate(Object in, Object out){
        String val = calculateValue(in, out);
        try {
            if(targetField != null) {
                FieldUtils.writeField(targetField, out, val, true);
            }
        } catch (IllegalAccessException e) {
            LOG.error("", e);
        }
    }

    @Override
    public String calculateValue(Object in, Object out){
        String val = null;
        if(sourceFields != null)
            val = JAXBUtils.computeId(in, sourceFields);
        return String.format("%s%s%s", iriPrefix, val, iriPostfix);
    }

    protected <T> T getValue(Function<SetIRI, T> get, Predicate<T> condition, T defaultValue){
        return Stream.of(setIRI, setIRIOnClass)
                .filter(a -> a != null)
                .map(get)
                .filter(condition)
                .findFirst().orElse(defaultValue);
    }

//    public static IRIDecorator construct(Method transformDeclaration){
//        IRIDecorator iriDecorator = new IRIDecorator();
//        boolean ret = iriDecorator.configure(transformDeclaration);
//
//        return ret ? iriDecorator : null;
//    }

}
