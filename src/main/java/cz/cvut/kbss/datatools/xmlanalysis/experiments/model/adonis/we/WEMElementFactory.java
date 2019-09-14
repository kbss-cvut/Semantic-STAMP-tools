package cz.cvut.kbss.datatools.xmlanalysis.experiments.model.adonis.we;

import cz.cvut.kbss.datatools.xmlanalysis.experiments.model.ModelElementFactory;

import java.util.HashMap;

public class WEMElementFactory extends ModelElementFactory {


    public WEMElementFactory() {
        HashMap<String, Class> modelElementNameToClassMap = new HashMap<>();
        modelElementNameToClassMap.put("Organizational unit", OrganizationalUnit.class);
        modelElementNameToClassMap.put("Performer", Performer.class);
        modelElementNameToClassMap.put("Role", Role.class);
        modelElementNameToClassMap.put("Belongs to", BelongsTo.class);
        modelElementNameToClassMap.put("Hase role", HasRole.class);
        setModelElementNameToClassMap(modelElementNameToClassMap);
    }

}
