package cz.cvut.kbss.inbas.reporting.service.arms;

import cz.cvut.kbss.inbas.reporting.model.OccurrenceReport;

import java.net.URI;

public interface ArmsService {

    /**
     * Calculates ARMS index of the specified report.
     * <p>
     * The calculation is based on the table in https://essi.easa.europa.eu/documents/Methodology.pdf, page 19.
     *
     * @param report Report whose ARMS index will be calculated
     * @return ARMS index of the specified report, or 0, if any of the required values is missing
     */
    int calculateArmsIndex(OccurrenceReport report);

    /**
     * Calculates ARMS index from the specified values.
     * <p>
     * The calculation is based on the table in https://essi.easa.europa.eu/documents/Methodology.pdf, page 19.
     *
     * @return ARMS index of the specified report, or 0, if either of the required values is missing or invalid
     */
    int calculateArmsIndex(URI accidentOutcome, URI barrierEffectiveness);
}
