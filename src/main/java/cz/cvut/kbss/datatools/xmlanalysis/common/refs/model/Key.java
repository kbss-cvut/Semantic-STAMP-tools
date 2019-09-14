package cz.cvut.kbss.datatools.xmlanalysis.common.refs.model;

import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class Key {
    protected String keyName;
    protected Class cls;
    protected Class refedClass;
    protected boolean isManyFK;
    protected List<Pair<String, Field>> fields;

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public Class getCls() {
        return cls;
    }

    public void setCls(Class cls) {
        this.cls = cls;
    }


    /**
     * Note that in this implementation the returned List of fields is unmodifiable.
     * @return
     */
    public List<Pair<String,Field>> getFields() {
        return fields;
    }

    /**
     * Set the fields which form a key.
     * Note : The input list is copied to an unmodifiable list. This is done to simplify synchronization of the state of
     * this property and the internal state of a Key object which is based on the state of this property.
     * @param fields
     */
    public void setFields(List<Pair<String,Field>> fields) {
        fields = new ArrayList(fields);
        fields.sort(Comparator.comparing(p -> p.getKey()));
        resetCache();
        this.fields = Collections.unmodifiableList(fields); // avoid changes to the list when retrieved from getFields.
    }

    public Class getRefedClass() {
        return refedClass;
    }

    public void setRefedClass(Class refedClass) {
        this.refedClass = refedClass;
    }

    public boolean isManyFK() {
        return isManyFK;
    }

    public void setManyFK(boolean manyFK) {
        isManyFK = manyFK;
    }

    protected String _fieldsIdCache = null;
    protected Integer _hashCache = null;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Key key = (Key) o;
        return keyName.equals(key.keyName) &&
                cls.equals(key.cls) &&
                refedClass.equals(key.refedClass) &&
                fieldsId().equals(key.fieldsId());
    }


    @Override
    public int hashCode() {
        if(_hashCache == null){
            _hashCache = Objects.hash(keyName, cls, refedClass, fieldsId(), isManyFK);
        }
        return _hashCache;
    }

    protected String fieldsId(){
        if(_fieldsIdCache == null){
             _fieldsIdCache = fields.stream().map(p -> p.getLeft() + ", " + p.getRight().getName()).collect(Collectors.joining("-"));
        }
        return _fieldsIdCache;
    }


    protected synchronized void resetCache(){
        _hashCache = null;
        _fieldsIdCache = null;
    }
}
