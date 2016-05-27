package cz.cvut.kbss.inbas.reporting.service.options;

import cz.cvut.kbss.inbas.reporting.rest.dto.model.RawJson;
import cz.cvut.kbss.inbas.reporting.util.JsonLdProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportingPhaseService {

    private static final String PHASE_OPTION_TYPE = "reportingPhase";
    private static final String GREATER_THAN_PROPERTY = "http://onto.fel.cvut.cz/ontologies/aviation-safety/is-higher-than";

    @Autowired
    private OptionsService optionsService;

    private final List<URI> phases = new ArrayList<>();

    @PostConstruct
    private void loadPhases() {
        final RawJson json = (RawJson) optionsService.getOptions(PHASE_OPTION_TYPE);
        this.phases.addAll(JsonLdProcessing.getOrderedOptions(json, GREATER_THAN_PROPERTY));
    }

    /**
     * Gets the initial reporting phase.
     *
     * @return Initial reporting phase
     */
    public URI getInitialPhase() {
        if (phases.isEmpty()) {
            throw new IllegalStateException("No reporting phases have been found.");
        }
        return phases.get(0);
    }

    /**
     * Gets reporting phase which immediately follows the specified phase.
     * <p>
     * In case the current phase is {@code null}, {@code null} is also returned. If there is no phase after {@code
     * currentPhase}, the {@code currentPhase} is also returned.
     *
     * @param currentPhase Phase whose successor should be returned
     * @return Next reporting phase
     */
    public URI nextPhase(URI currentPhase) {
        if (currentPhase == null) {
            return null;
        }
        final int i = phases.indexOf(currentPhase);
        if (i == -1) {
            throw new IllegalArgumentException("Unsupported reporting phase " + currentPhase);
        }
        return phases.get(i == phases.size() - 1 ? i : i + 1);
    }
}
