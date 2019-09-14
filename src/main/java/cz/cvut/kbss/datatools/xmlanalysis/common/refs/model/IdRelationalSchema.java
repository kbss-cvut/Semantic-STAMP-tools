package cz.cvut.kbss.datatools.xmlanalysis.common.refs.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdRelationalSchema {

    protected List<RelationField> fields = new ArrayList<>();

    protected List<KeyMapping> keyMappings =  new ArrayList<>();

    protected Map<Class, ClassMetadata> class2MetadataMap = new HashMap<>();

//    protected List<ClassMetadata> classes = new ArrayList<>();

    public List<RelationField> getFields() {
        return fields;
    }

    public void setFields(List<RelationField> fields) {
        this.fields = fields;
    }

    public List<KeyMapping> getKeyMappings() {
        return keyMappings;
    }

    public void setKeyMappings(List<KeyMapping> keyMappings) {
        this.keyMappings = keyMappings;
    }

    public Map<Class, ClassMetadata> getClass2MetadataMap() {
        return class2MetadataMap;
    }

    public void setClass2MetadataMap(Map<Class, ClassMetadata> class2MetadataMap) {
        this.class2MetadataMap = class2MetadataMap;
    }




//    public static class IDMetadataIndex{
//        protected Class cls;
//        protected List<RelationField> relations;
//        protected List<KeyMapping> keyMappings;
//
//        public IDMetadataIndex() {
//        }
//
//        public IDMetadataIndex(Class cls, List<RelationField> relations, List<KeyMapping> keyMappings) {
//            this.cls = cls;
//            this.relations = relations;
//            this.keyMappings = keyMappings;
//        }
//
//        public Class getCls() {
//            return cls;
//        }
//
//        public void setCls(Class cls) {
//            this.cls = cls;
//        }
//
//        public List<RelationField> getRelations() {
//            return relations;
//        }
//
//        public void setRelations(List<RelationField> relations) {
//            this.relations = relations;
//        }
//
//        public List<KeyMapping> getKeyMappings() {
//            return keyMappings;
//        }
//
//        public void setKeyMappings(List<KeyMapping> keyMappings) {
//            this.keyMappings = keyMappings;
//        }
//    }


}
