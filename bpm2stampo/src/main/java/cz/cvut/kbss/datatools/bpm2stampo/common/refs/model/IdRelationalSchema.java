package cz.cvut.kbss.datatools.bpm2stampo.common.refs.model;

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

        Set<Map.Entry<Class, ClassMetadata>> onlyIn1 = new HashSet<>();
        Set<Map.Entry<Class, ClassMetadata>> inBoth = new HashSet<>();

        for(Map.Entry<Class, ClassMetadata>  e1 : class2MetadataMap.entrySet()) {
            boolean in2 = false;
            for (Map.Entry<Class, ClassMetadata> e2 : schema.class2MetadataMap.entrySet()) {
                if(e1.getKey().equals(e2.getKey())){
                    if(e1.getValue().equals(e2.getValue())){
                        in2 = true;
                        inBoth.add(e1);
                    }
                }
            }
            if(!in2){
                onlyIn1.add(e1);
            }
        }

        return
                Objects.equals(class2MetadataMap, schema.class2MetadataMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(class2MetadataMap);
    }

}
