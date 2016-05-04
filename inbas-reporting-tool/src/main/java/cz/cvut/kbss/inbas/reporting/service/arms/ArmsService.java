package cz.cvut.kbss.inbas.reporting.service.arms;

import cz.cvut.kbss.inbas.reporting.model.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.model.arms.AccidentOutcome;
import cz.cvut.kbss.inbas.reporting.model.arms.BarrierEffectiveness;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * Service for ARMS-related business logic.
 */
@Service
public class ArmsService {

    private static final Map<AccidentOutcome, Map<BarrierEffectiveness, Short>> ARMS_TABLE = initArmsTable();

    private static Map<AccidentOutcome, Map<BarrierEffectiveness, Short>> initArmsTable() {
        final Map<AccidentOutcome, Map<BarrierEffectiveness, Short>> armsTable = new EnumMap<>(AccidentOutcome.class);
        final Map<BarrierEffectiveness, Short> negligible = new EnumMap<>(BarrierEffectiveness.class);
        negligible.put(BarrierEffectiveness.EFFECTIVE, (short) 1);
        negligible.put(BarrierEffectiveness.LIMITED, (short) 1);
        negligible.put(BarrierEffectiveness.MINIMAL, (short) 1);
        negligible.put(BarrierEffectiveness.NOT_EFFECTIVE, (short) 1);
        armsTable.put(AccidentOutcome.NEGLIGIBLE, negligible);
        final Map<BarrierEffectiveness, Short> minor = new EnumMap<>(BarrierEffectiveness.class);
        minor.put(BarrierEffectiveness.EFFECTIVE, (short) 2);
        minor.put(BarrierEffectiveness.LIMITED, (short) 4);
        minor.put(BarrierEffectiveness.MINIMAL, (short) 20);
        minor.put(BarrierEffectiveness.NOT_EFFECTIVE, (short) 100);
        armsTable.put(AccidentOutcome.MINOR, minor);
        final Map<BarrierEffectiveness, Short> major = new EnumMap<>(BarrierEffectiveness.class);
        major.put(BarrierEffectiveness.EFFECTIVE, (short) 10);
        major.put(BarrierEffectiveness.LIMITED, (short) 20);
        major.put(BarrierEffectiveness.MINIMAL, (short) 100);
        major.put(BarrierEffectiveness.NOT_EFFECTIVE, (short) 500);
        armsTable.put(AccidentOutcome.MAJOR, major);
        final Map<BarrierEffectiveness, Short> catastrophic = new EnumMap<>(BarrierEffectiveness.class);
        catastrophic.put(BarrierEffectiveness.EFFECTIVE, (short) 50);
        catastrophic.put(BarrierEffectiveness.LIMITED, (short) 100);
        catastrophic.put(BarrierEffectiveness.MINIMAL, (short) 500);
        catastrophic.put(BarrierEffectiveness.NOT_EFFECTIVE, (short) 2500);
        armsTable.put(AccidentOutcome.CATASTROPHIC, catastrophic);
        return armsTable;
    }

    /**
     * Calculates ARMS index of the specified report.
     * <p>
     * The calculation is based on the table in http://essi.easa.europa.eu/documents/ARMS.pdf, slide 27.
     *
     * @param report Report whose ARMS index will be calculated
     * @return ARMS index of the specified report, or 0, if any of the required values is missing
     */
    public short calculateArmsIndex(OccurrenceReport report) {
        Objects.requireNonNull(report);
        return calculateArmsIndex(report.getAccidentOutcome(), report.getBarrierEffectiveness());
    }

    /**
     * Calculates ARMS index from the specified values.
     * <p>
     * The calculation is based on the table in http://essi.easa.europa.eu/documents/ARMS.pdf, slide 27.
     *
     * @return ARMS index of the specified report, or 0, if either of the required values is missing
     */
    public short calculateArmsIndex(AccidentOutcome accidentOutcome, BarrierEffectiveness barrierEffectiveness) {
        if (accidentOutcome == null || barrierEffectiveness == null) {
            return 0;
        }
        return ARMS_TABLE.get(accidentOutcome).get(barrierEffectiveness);
    }
}
