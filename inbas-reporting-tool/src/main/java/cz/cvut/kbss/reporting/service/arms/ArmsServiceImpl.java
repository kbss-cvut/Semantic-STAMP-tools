package cz.cvut.kbss.reporting.service.arms;

import cz.cvut.kbss.reporting.exception.JsonProcessingException;
import cz.cvut.kbss.reporting.model.OccurrenceReport;
import cz.cvut.kbss.reporting.model.safetyissue.SafetyIssueRiskAssessment;
import cz.cvut.kbss.reporting.rest.dto.model.RawJson;
import cz.cvut.kbss.reporting.service.options.OptionsService;
import cz.cvut.kbss.reporting.util.JsonLdProcessing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.*;

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

    private static final String INITIAL_EVENT_FREQUENCY_PARAM = "initialEventFrequency";
    private static final String BARRIER_UOS_AVOIDANCE_FAIL_FREQUENCY_PARAM = "barrierUosAvoidanceFailFrequency";
    private static final String BARRIER_RECOVERY_FAIL_FREQUENCY_PARAM = "barrierRecoveryFailFrequency";
    private static final String ACCIDENT_SEVERITY_PARAM = "accidentSeverity";
    private static final String SIRA_PARAM = "sira";

    private static final String GREATER_THAN_URI = "http://onto.fel.cvut.cz/ontologies/arms/sira/model/is-higher-than";

    @Autowired
    private OptionsService optionsService;

    private List<URI> accidents;
    private List<URI> barriers;

    // SIRA
    private Map<URI, SiraOption> initialEventFrequencies;
    private Map<URI, SiraOption> barrierUosAvoidanceFailFrequencies;
    private Map<URI, SiraOption> barrierRecoveryFailFrequencies;
    private Map<URI, SiraOption> accidentSeverities;
    private List<SiraOption> siraValues;

    private boolean siraInitialized;

    @PostConstruct
    void initArmsAttributes() {
        try {
            this.accidents = initAccidents();
            this.barriers = initBarriers();
            this.initialEventFrequencies = readSiraOptions(INITIAL_EVENT_FREQUENCY_PARAM);
            this.barrierUosAvoidanceFailFrequencies = readSiraOptions(BARRIER_UOS_AVOIDANCE_FAIL_FREQUENCY_PARAM);
            this.barrierRecoveryFailFrequencies = readSiraOptions(BARRIER_RECOVERY_FAIL_FREQUENCY_PARAM);
            this.accidentSeverities = readSiraOptions(ACCIDENT_SEVERITY_PARAM);
            this.siraValues = readSiraValues();
            this.siraInitialized = true;
        } catch (IOException | JsonProcessingException e) {
            LOG.error("Unable to initialize ARMS or SIRA values.", e);
            this.accidents = Collections.emptyList();
            this.barriers = Collections.emptyList();
            this.initialEventFrequencies = Collections.emptyMap();
            this.barrierUosAvoidanceFailFrequencies = Collections.emptyMap();
            this.barrierRecoveryFailFrequencies = Collections.emptyMap();
            this.accidentSeverities = Collections.emptyMap();
            this.siraValues = Collections.emptyList();
        }
    }

    private List<URI> initAccidents() throws IOException {
        final RawJson json = (RawJson) optionsService.getOptions(ACCIDENT_OUTCOME_PARAM, Collections.emptyMap());
        return JsonLdProcessing.getOrderedOptions(json, GREATER_THAN_URI);
    }

    private List<URI> initBarriers() throws IOException {
        final RawJson json = (RawJson) optionsService.getOptions(BARRIER_EFFECTIVENESS_PARAM, Collections.emptyMap());
        return JsonLdProcessing.getOrderedOptions(json, GREATER_THAN_URI);
    }

    private Map<URI, SiraOption> readSiraOptions(String paramName) {
        final RawJson json = (RawJson) optionsService.getOptions(paramName, Collections.emptyMap());
        final List<SiraOption> options = JsonLdProcessing.readFromJsonLd(json, SiraOption.class);
        final Map<URI, SiraOption> map = new HashMap<>(options.size());
        options.forEach(o -> map.put(o.getUri(), o));
        return map;
    }

    private List<SiraOption> readSiraValues() {
        final RawJson json = (RawJson) optionsService.getOptions(SIRA_PARAM, Collections.emptyMap());
        final List<SiraOption> options = JsonLdProcessing.readFromJsonLd(json, SiraOption.class);
        Collections.sort(options);
        return options;
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
        final int outcomeIndex = index;
        index = 0;
        while (index < barriers.size() && !barriers.get(index).equals(barrierEffectiveness)) {
            value *= index < 1 ? FIRST_STEP_FACTOR : STEP_FACTOR;
            index++;
        }
        if (index == barriers.size()) {
            return 0;
        }
        // Modification of the index, according to ARMS 4.1
        if (index > 0 && index < barriers.size() - 1) {
            if (outcomeIndex == accidents.size() - 1) {
                value += 2;
            } else if (outcomeIndex == accidents.size() - 2) {
                value += 1;
            }
        }
        return value;
    }

    @Override
    public URI calculateSafetyIssueRiskAssessment(SafetyIssueRiskAssessment sira) {
        Objects.requireNonNull(sira);
        if (sira.getInitialEventFrequency() == null || sira.getBarrierUosAvoidanceFailFrequency() == null ||
                sira.getBarrierRecoveryFailFrequency() == null || sira.getAccidentSeverity() == null) {
            LOG.warn("Missing at least one of the values necessary for computing SIRA. {}", sira);
            return null;
        }
        if (!siraInitialized) {
            LOG.warn("Cannot calculate SIRA, options have not been initialized.");
            return null;
        }
        final SiraOption initialEventFrequency = initialEventFrequencies.get(sira.getInitialEventFrequency());
        final SiraOption barrierUosAvoidanceFailFrequency = barrierUosAvoidanceFailFrequencies
                .get(sira.getBarrierUosAvoidanceFailFrequency());
        final SiraOption barrierRecoveryFailFrequency = barrierRecoveryFailFrequencies
                .get(sira.getBarrierRecoveryFailFrequency());
        final SiraOption accidentSeverity = accidentSeverities.get(sira.getAccidentSeverity());
        if (initialEventFrequency == null || barrierUosAvoidanceFailFrequency == null ||
                barrierRecoveryFailFrequency == null || accidentSeverity == null) {
            throw new IllegalArgumentException("One of the risk assessment attribute values is invalid. " + sira);
        }

        return calculateSiraValue(initialEventFrequency, barrierUosAvoidanceFailFrequency, barrierRecoveryFailFrequency,
                accidentSeverity);
    }

    private URI calculateSiraValue(SiraOption initialEventFrequency, SiraOption barrierUosAvoidanceFailFrequency,
                                   SiraOption barrierRecoveryFailFrequency, SiraOption accidentSeverity) {
        double value = initialEventFrequency.getDataValue() * barrierUosAvoidanceFailFrequency.getDataValue() *
                barrierRecoveryFailFrequency.getDataValue() / accidentSeverity.getDataValue();
        // Use BigDecimal and rounding to prevent double precision issues
        BigDecimal decimal = new BigDecimal(value);
        decimal = decimal.setScale(3, BigDecimal.ROUND_HALF_UP);
        SiraOption result = siraValues.get(0);
        for (SiraOption siraValue : siraValues) {
            BigDecimal bdSiraVal = new BigDecimal(siraValue.getDataValue());
            bdSiraVal = bdSiraVal.setScale(3, BigDecimal.ROUND_HALF_UP);
            if (bdSiraVal.compareTo(decimal) > 0) {
                break;
            }
            result = siraValue;
        }
        return result.getUri();
    }
}
