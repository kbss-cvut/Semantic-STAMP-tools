package cz.cvut.kbss.inbas.audit.service;

import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;

import java.util.List;

public interface PreliminaryReportService extends BaseService<PreliminaryReport> {

    PreliminaryReport findByKey(String key);

    List<PreliminaryReport> findByOccurrence(Occurrence occurrence);
}
