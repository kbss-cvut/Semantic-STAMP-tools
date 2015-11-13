package cz.cvut.kbss.inbas.audit.service;

import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.ReportingPhase;
import cz.cvut.kbss.inbas.audit.model.reports.Report;

import java.util.Collection;

public interface OccurrenceService extends BaseService<Occurrence> {

    Occurrence findByKey(String key);

    Collection<Report> getReports(Occurrence occurrence);

    Collection<Report> getReports(Occurrence occurrence, ReportingPhase phase);
}
