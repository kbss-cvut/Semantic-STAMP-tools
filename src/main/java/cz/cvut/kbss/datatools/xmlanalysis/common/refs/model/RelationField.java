package cz.cvut.kbss.datatools.xmlanalysis.common.refs.model;

import java.lang.reflect.Field;

public class RelationField {
    protected Field relationField;
    protected Field containerField;
    protected KeyMapping keyMapping;
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
}
