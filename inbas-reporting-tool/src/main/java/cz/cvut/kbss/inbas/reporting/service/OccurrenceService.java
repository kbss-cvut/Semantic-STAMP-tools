package cz.cvut.kbss.inbas.reporting.service;

import cz.cvut.kbss.inbas.reporting.model.ReportingPhase;
import cz.cvut.kbss.inbas.reporting.model.reports.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.model_new.Occurrence;

import java.util.Collection;

public interface OccurrenceService extends BaseService<Occurrence> {

    Occurrence findByKey(String key);

    Collection<OccurrenceReport> getReports(Occurrence occurrence);

    Collection<OccurrenceReport> getReports(Occurrence occurrence, ReportingPhase phase);
}
