package cz.cvut.kbss.datatools.xmlanalysis.common.refs.model;

import cz.cvut.kbss.datatools.xmlanalysis.common.refs.annotations.RelationTypes;

import java.util.Objects;

/**
 * A model for key mappings.
 */
public class KeyMapping {

//    protected boolean isManyToOneFK = false;
//
//    protected boolean isOneToManyFK = false;

    protected RelationTypes relationType;

    /** the foreign key */
    protected Key foreignKey;
    /** the key */
    protected Key key;

    public KeyMapping() {
    }

    public KeyMapping(Key foreignKey, Key key, RelationTypes relationType) {
        this.foreignKey = foreignKey;
        this.key = key;
        this.relationType = relationType;
    }

    public KeyMapping(Key foreignKey, Key key, boolean isManyToOneFK, boolean isOne2ManyFK) {
        this.foreignKey = foreignKey;
        this.key = key;
//        this.isManyToOneFK = isManyToOneFK;
//        this.isOneToManyFK = isOne2ManyFK;
    }

    public Key getForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(Key foreignKey) {
        this.foreignKey = foreignKey;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public RelationTypes getRelationType() {
        return relationType;
    }

    public void setRelationType(RelationTypes relationType) {
        this.relationType = relationType;
    }

    //    public boolean isManyToOneFK() {
//        return isManyToOneFK;
//    }
//
//    public void setManyToOneFK(boolean manyToOneFK) {
//        isManyToOneFK = manyToOneFK;
//    }
//
//    public boolean isOneToManyFK() {
//        return isOneToManyFK;
//    }
//
//    public void setOneToManyFK(boolean oneToManyFK) {
//        isOneToManyFK = oneToManyFK;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyMapping that = (KeyMapping) o;
        return
//                isManyToOneFK == that.isManyToOneFK &&
//                isOneToManyFK == that.isOneToManyFK &&
                relationType  == that.relationType &&
                Objects.equals(foreignKey, that.foreignKey) &&
                Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
//        return Objects.hash(isManyToOneFK, isOneToManyFK, foreignKey, key);
        return Objects.hash(relationType, foreignKey, key);
    }
}
