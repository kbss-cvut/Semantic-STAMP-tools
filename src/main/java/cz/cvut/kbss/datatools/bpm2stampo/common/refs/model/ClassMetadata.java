package cz.cvut.kbss.datatools.bpm2stampo.common.refs.model;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A model of a class, its keys, key-mappings (key <-> foreign keys) and its relational fields(the ones where instances
 * should be injected). Use-cases:
 * - find the class metadata of an instance
 * -
 */
public class ClassMetadata {
    protected Set<Key> toOneKeys = new HashSet<>();
    protected Set<Key> oneToManyKeys = new HashSet<>();;
    protected Set<KeyMapping> keyMappings;
    protected Set<RelationField> relations;
    protected Set<RelationField> farRelations = new HashSet<>();


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

    public Set<Key> getToOneKeys() {
        return toOneKeys;
    }

    public void setToOneKeys(Set<Key> toOneKeys) {
        this.toOneKeys = toOneKeys;
    }

    public Set<Key> getOneToManyKeys() {
        return oneToManyKeys;
    }

    public void setOneToManyKeys(Set<Key> oneToManyKeys) {
        this.oneToManyKeys = oneToManyKeys;
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

    public Set<RelationField> getFarRelations() {
        return farRelations;
    }

    public void setFarRelations(Set<RelationField> farRelations) {
        this.farRelations = farRelations;
    }

    /**
     * Builds and populates internal data structures used for object injection when used in the IDRuntime
     */
    public void addKey(Key key, KeyMapping keyMapping){
        switch(keyMapping.getRelationType()){
            case manyToMany:
            case oneToMany:
                break;

        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassMetadata that = (ClassMetadata) o;
        return CompareUtils.equalsAsSets(toOneKeys, that.toOneKeys) &&
                CompareUtils.equalsAsSets(oneToManyKeys, that.oneToManyKeys) &&
                CompareUtils.equalsAsSets(keyMappings, that.keyMappings) &&
                CompareUtils.equalsAsSets(relations, that.relations) && // TODO the last two sets not compare correctly
                CompareUtils.equalsAsSets(farRelations, that.farRelations);
    }

    @Override
    public int hashCode() {
        ArrayList a;
        HashSet s;
        return Objects.hash(toOneKeys, oneToManyKeys, keyMappings, relations, farRelations);
    }
}
