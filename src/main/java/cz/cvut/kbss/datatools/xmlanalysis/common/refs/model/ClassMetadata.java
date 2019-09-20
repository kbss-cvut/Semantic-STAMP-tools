package cz.cvut.kbss.datatools.xmlanalysis.common.refs.model;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A model of a class, its keys, key-mappings (key <-> foreign keys) and its relational fields(the ones where instances
 * should be injected). Use-cases:
 * - find the class metadata of an instance
 * -
 */
public class ClassMetadata {
    protected Set<Key> keys;
    protected Set<KeyMapping> keyMappings;
    protected Set<RelationField> relations;


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

    public Set<KeyMapping> getKeyMappings() {
        return keyMappings;
    }

    public void setKeyMappings(Set<KeyMapping> keyMappings) {
        this.keyMappings = keyMappings;
    }

    public Set<RelationField> getRelations() {
        return relations;
    }

    public void setRelations(Set<RelationField> relations) {
        this.relations = relations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassMetadata that = (ClassMetadata) o;
        return CompareUtils.equalsAsSets(keys, that.keys) &&
                CompareUtils.equalsAsSets(keyMappings, that.keyMappings) &&
                CompareUtils.equalsAsSets(relations, that.relations);
    }

    @Override
    public int hashCode() {
        ArrayList a;
        HashSet s;
        return Objects.hash(keys, keyMappings, relations);
    }
}
