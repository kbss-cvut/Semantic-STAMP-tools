package cz.cvut.kbss.datatools.xmlanalysis.common.refs.model;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassMetadata {
    protected Set<Key> keys;
    protected List<KeyMapping> KeyMappings;
    protected List<RelationField> relations;
//    protected List<RelationField> inverseRelations; ? do we need that?


    public KeyMapping getKeyMapping(String keyName){
         List<KeyMapping> mappings = getKeyMappings().stream()
                .filter(km -> Objects.equals(km.getForeignKey().getKeyName(), keyName))
                 .collect(Collectors.toList());
         switch (mappings.size()){
             case 1: return mappings.get(0);
             case 0: throw new RuntimeException(String.format("No key mapping with name '%s' found", keyName));
             default: throw new RuntimeException("Ambiguos association of Relation and FK, using unnamed FK reference field");
         }
    }

    public Set<Key> getKeys() {
        return keys;
    }

    public void setKeys(Set<Key> keys) {
        this.keys = keys;
    }

    public List<KeyMapping> getKeyMappings() {
        return KeyMappings;
    }

    public void setKeyMappings(List<KeyMapping> keyMappings) {
        KeyMappings = keyMappings;
    }

    public List<RelationField> getRelations() {
        return relations;
    }

    public void setRelations(List<RelationField> relations) {
        this.relations = relations;
    }
}
