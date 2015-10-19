package cz.cvut.kbss.inbas.audit.services.options;

import cz.cvut.kbss.inbas.audit.rest.dto.model.RawJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class TypeaheadOptionsService {

    @Autowired
    @Qualifier("eventType")
    private OptionsService eventTypeOptions;

    @Autowired
    @Qualifier("location")
    private OptionsService locationOptions;

    @Autowired
    @Qualifier("operator")
    private OptionsService operatorOptions;

    public RawJson getOptions(String type) {
        return getService(type).getOptions();
    }

    private OptionsService getService(String type) {
        switch (type) {
            case "eventType":
                return eventTypeOptions;
            case "location":
                return locationOptions;
            case "operator":
                return operatorOptions;
            default:
                throw new IllegalArgumentException("Unsupported options type " + type);
        }
    }
}
