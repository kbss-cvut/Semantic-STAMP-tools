package cz.cvut.kbss.inbas.reporting.service.arms;

import cz.cvut.kbss.inbas.reporting.exception.JsonProcessingException;
import cz.cvut.kbss.inbas.reporting.model.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.rest.dto.model.RawJson;
import cz.cvut.kbss.inbas.reporting.service.options.OptionsService;
import cz.cvut.kbss.inbas.reporting.util.JsonLdProcessing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Service for ARMS-related business logic.
 */
public class ArmsServiceImpl implements ArmsService {

    private static final Logger LOG = LoggerFactory.getLogger(ArmsServiceImpl.class);

    private static final int BASE_ARMS_INDEX_VALUE = 1;
    // From the first to the second column/row, the value is multiplied by this factor
    private static final int FIRST_STEP_FACTOR = 2;
    // For every other column/row transition, the value is multiplied by this factor
    private static final int STEP_FACTOR = 5;

    private static final String ACCIDENT_OUTCOME_PARAM = "accidentOutcome";
    private static final String BARRIER_EFFECTIVENESS_PARAM = "barrierEffectiveness";
    private static final String GREATER_THAN_URI = "http://onto.fel.cvut.cz/ontologies/arms/sira/model/is-higher-than";

    @Autowired
    private OptionsService optionsService;

    private List<URI> accidents;
    private List<URI> barriers;

    @PostConstruct
    void initArmsAttributes() {
        try {
            this.accidents = initAccidents();
            this.barriers = initBarriers();
        } catch (IOException | JsonProcessingException e) {
            LOG.error("Unable to initialize ARMS values.", e);
            this.accidents = Collections.emptyList();
            this.barriers = Collections.emptyList();
        }
    }

    private List<URI> initAccidents() throws IOException {
        final RawJson json = (RawJson) optionsService.getOptions(ACCIDENT_OUTCOME_PARAM);
        return JsonLdProcessing.getOrderedOptions(json, GREATER_THAN_URI);
    }

    private List<URI> initBarriers() throws IOException {
        final RawJson json = (RawJson) optionsService.getOptions(BARRIER_EFFECTIVENESS_PARAM);
        return JsonLdProcessing.getOrderedOptions(json, GREATER_THAN_URI);
    }

    @Override
    public int calculateArmsIndex(OccurrenceReport report) {
        Objects.requireNonNull(report);
        return calculateArmsIndex(report.getAccidentOutcome(), report.getBarrierEffectiveness());
    }

    @Override
    public int calculateArmsIndex(URI accidentOutcome, URI barrierEffectiveness) {
        if (accidentOutcome == null || barrierEffectiveness == null) {
            return 0;
        }
        int value = BASE_ARMS_INDEX_VALUE;
        int index = 0;
        while (index < accidents.size() && !accidents.get(index).equals(accidentOutcome)) {
            value *= index < 1 ? FIRST_STEP_FACTOR : STEP_FACTOR;
            index++;
        }
        if (index == accidents.size()) {
            return 0;
        }
        if (index == 0) {   // If the accident is the least serious, return base value immediately
            return value;
        }
        index = 0;
        while (index < barriers.size() && !barriers.get(index).equals(barrierEffectiveness)) {
            value *= index < 1 ? FIRST_STEP_FACTOR : STEP_FACTOR;
            index++;
        }
        if (index == barriers.size()) {
            return 0;
        }
        return value;
    }
}
