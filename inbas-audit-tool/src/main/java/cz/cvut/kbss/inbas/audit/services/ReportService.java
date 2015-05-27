package cz.cvut.kbss.inbas.audit.services;

import cz.cvut.kbss.inbas.audit.model.EventReport;

/**
 * @author ledvima1
 */
public interface ReportService extends InbasService<EventReport> {

    EventReport findByKey(String key);
}
