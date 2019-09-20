package cz.cvut.kbss.datatools.xmlanalysis.common.refs.model;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * A model of fields which are subject to injection. Use-cases:
 * - injection of instances as field values or elements of collections fields
 */
public class RelationField {

    /** The fields in which the references are injected */
    protected Field relationField;
    /** The field which contains the key mapping. This is used in case the key mapping is not in the same class as the
     * Relational field*/
    protected Field containerField;
    /** The key mapping used to find the instance(s) to be injected in the relational field*/
    protected KeyMapping keyMapping;
    /** True if the relational field is a collection.*/
    protected boolean isManyRelation;

    public Field getRelationField() {
        return relationField;
    }

    public void setRelationField(Field relationField) {
        this.relationField = relationField;
    }

    public Field getContainerField() {
        return containerField;
    }

    public void setContainerField(Field containerField) {
        this.containerField = containerField;
    }

    public KeyMapping getKeyMapping() {
        return keyMapping;
    }

    public void setKeyMapping(KeyMapping keyMapping) {
        this.keyMapping = keyMapping;
    }

    public boolean isManyRelation() {
        return isManyRelation;
    }

    public void setManyRelation(boolean manyRelation) {
        isManyRelation = manyRelation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RelationField that = (RelationField) o;
        return isManyRelation == that.isManyRelation &&
                Objects.equals(relationField, that.relationField) &&
                Objects.equals(containerField, that.containerField) &&
                Objects.equals(keyMapping, that.keyMapping);
    }

    @Override
    public int hashCode() {
        return Objects.hash(relationField, containerField, keyMapping, isManyRelation);
    }
}
