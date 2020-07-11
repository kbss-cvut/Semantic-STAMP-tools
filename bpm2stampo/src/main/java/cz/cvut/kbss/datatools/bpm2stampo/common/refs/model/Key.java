package cz.cvut.kbss.datatools.bpm2stampo.common.refs.model;

import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A model of a key definition. Keys are used in the following use-cases:
 * - given an instance of class "cls" construct its value for this key definition
 * - used in the model of a key-mapping.
 */
public class Key {
    /** the name of the key. If one class has multiple keys each of them are named. This way if there is a foreign key
     pointing to instances of such a class, the foreign key can specify which named-key is to be compared to.*/
    protected String keyName;
    /** The class which contains the key fields*/
    protected Class cls;
    /** The class which is being referred to by the key. If this key is not a foreign key, the value of this field is the
     same as the csl field. */
    protected Class refedClass;
    /** The key fields. The String in the pair is the key field name which corresponds to the mapped name of the corresponding key
     field in refedClass. Together with the value of the fields the key field names are used to generate a unique key
     value. */
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
            _hashCache = Objects.hash(keyName, cls, refedClass, fieldsId());
        }
        return _hashCache;
    }

    protected String fieldsId(){
        if(_fieldsIdCache == null){
             _fieldsIdCache = fields.stream().map(p -> p.getLeft() + "-" + p.getRight().getName()).collect(Collectors.joining(";"));
        }
        return _fieldsIdCache;
    }


    protected synchronized void resetCache(){
        _hashCache = null;
        _fieldsIdCache = null;
    }
}
