package cz.cvut.kbss.inbas.audit.service.options;

import cz.cvut.kbss.inbas.audit.rest.dto.model.RawJson;
import cz.cvut.kbss.inbas.audit.util.FileDataLoader;
import org.springframework.stereotype.Service;

@Service("location")
class LocationOptionsService implements OptionsService {

    private String locations;

    @Override
    public synchronized RawJson getOptions() {
        if (locations == null) {
            loadLocations();
        }
        return new RawJson(locations);
    }

    private void loadLocations() {
        this.locations = new FileDataLoader().load("locations.json");
    }
}
