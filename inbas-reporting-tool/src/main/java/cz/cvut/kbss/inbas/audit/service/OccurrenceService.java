package cz.cvut.kbss.inbas.audit.service;

import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.ReportingPhase;
import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport;

import java.util.Collection;

public interface OccurrenceService extends BaseService<Occurrence> {

    Occurrence findByKey(String key);

    Collection<OccurrenceReport> getReports(Occurrence occurrence);

    Collection<OccurrenceReport> getReports(Occurrence occurrence, ReportingPhase phase);
}
