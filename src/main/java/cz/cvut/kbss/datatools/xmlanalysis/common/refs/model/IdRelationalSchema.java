package cz.cvut.kbss.datatools.xmlanalysis.common.refs.model;

import java.util.*;

public class IdRelationalSchema {

    protected Map<Class, ClassMetadata> class2MetadataMap = new HashMap<>();


    public Map<Class, ClassMetadata> getClass2MetadataMap() {
        return class2MetadataMap;
    }

    public void setClass2MetadataMap(Map<Class, ClassMetadata> class2MetadataMap) {
        this.class2MetadataMap = class2MetadataMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdRelationalSchema schema = (IdRelationalSchema) o;
        return
                Objects.equals(class2MetadataMap, schema.class2MetadataMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(class2MetadataMap);
    }

}
