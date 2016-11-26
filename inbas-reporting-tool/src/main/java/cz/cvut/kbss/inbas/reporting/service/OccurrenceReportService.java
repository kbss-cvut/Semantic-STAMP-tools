package cz.cvut.kbss.inbas.reporting.service;

import cz.cvut.kbss.inbas.reporting.exception.NotFoundException;
import cz.cvut.kbss.inbas.reporting.model.OccurrenceReport;

public interface OccurrenceReportService extends BaseReportService<OccurrenceReport> {

    /**
     * Creates new report revision in report chain with the specified number.
     * <p>
     * The new revision is created from the latest version of the report in ECCAIRS.
     *
     * @param fileNumber Report chain identifier
     * @return The new revision report
     * @throws NotFoundException If there is no ECCAIRS report corresponding to the chain
     */
    OccurrenceReport createNewRevisionFromEccairs(Long fileNumber);
}
