package cz.cvut.kbss.inbas.audit.services;

import cz.cvut.kbss.inbas.audit.model.OccurrenceReport;

/**
 * @author ledvima1
 */
public interface ReportService extends InbasService<OccurrenceReport> {

    OccurrenceReport findByKey(String key);
}
