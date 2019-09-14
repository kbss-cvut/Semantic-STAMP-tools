package cz.cvut.kbss.datatools.xmlanalysis.common.refs.model;

public class KeyMapping {
    protected boolean isManyFK = false;
    protected Key foreignKey;
    protected Key key;
//    protected

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
}
