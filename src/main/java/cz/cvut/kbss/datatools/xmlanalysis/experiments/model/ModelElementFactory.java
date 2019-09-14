package cz.cvut.kbss.datatools.xmlanalysis.experiments.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ModelElementFactory {
    private static final Logger LOG = LoggerFactory.getLogger(ModelElementFactory.class);

    public Map<String, Class> modelElementNameToClassMap;
    public Map<Class, String> classToModelElementNameMap;

    public void setModelElementNameToClassMap(Map<String, Class> modelElementNameToClassMap) {
        this.modelElementNameToClassMap = modelElementNameToClassMap;
        this.classToModelElementNameMap = new HashMap<>();
        modelElementNameToClassMap.forEach((k,v) -> classToModelElementNameMap.put(v ,k));
    }


    public Object createModelElement(String clazzName){
        try {
            Class cls = modelElementNameToClassMap.get(clazzName);
            return cls.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
