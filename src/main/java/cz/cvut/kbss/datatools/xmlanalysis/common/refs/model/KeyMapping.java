package cz.cvut.kbss.datatools.xmlanalysis.common.refs.model;

import java.util.Objects;

/**
 * A model for key mappings.
 */
public class KeyMapping {
    protected boolean isManyFK = false;
    /** the foreign key */
    protected Key foreignKey;
    /** the key */
    protected Key key;

    public KeyMapping() {
    }

    public KeyMapping(Key foreignKey, Key key, boolean isManyFK) {
        this.foreignKey = foreignKey;
        this.key = key;
        this.isManyFK = isManyFK;
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

    public boolean isManyFK() {
        return isManyFK;
    }

    public void setManyFK(boolean manyFK) {
        isManyFK = manyFK;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyMapping that = (KeyMapping) o;
        return isManyFK == that.isManyFK &&
                Objects.equals(foreignKey, that.foreignKey) &&
                Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isManyFK, foreignKey, key);
    }
}
